package com.cinemaroyale.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinemaroyale.dto.FuncionRequestDTO;
import com.cinemaroyale.dto.FuncionResponseDTO;
import com.cinemaroyale.exceptions.ResourceNotFoundException;
import com.cinemaroyale.exceptions.DuplicateResourceException;
import com.cinemaroyale.model.Asiento;
import com.cinemaroyale.model.EstadoAsiento;
import com.cinemaroyale.model.Funcion;
import com.cinemaroyale.model.Pelicula;
import com.cinemaroyale.model.Sala;
import com.cinemaroyale.repository.AsientoRepository;
import com.cinemaroyale.repository.EstadoAsientoRepository;
import com.cinemaroyale.repository.FuncionRepository;
import com.cinemaroyale.repository.PeliculaRepository;
import com.cinemaroyale.repository.SalaRepository;

@Service
public class FuncionServiceImpl implements FuncionService {

    private final FuncionRepository funcionRepo;
    private final PeliculaRepository peliculaRepo;
    private final SalaRepository salaRepo;
    private final AsientoRepository asientoRepo;
    private final EstadoAsientoRepository estadoAsientoRepo;

    public FuncionServiceImpl(FuncionRepository funcionRepo,
                              PeliculaRepository peliculaRepo,
                              SalaRepository salaRepo,
                              AsientoRepository asientoRepo,
                              EstadoAsientoRepository estadoAsientoRepo) {
        this.funcionRepo = funcionRepo;
        this.peliculaRepo = peliculaRepo;
        this.salaRepo = salaRepo;
        this.asientoRepo = asientoRepo;
        this.estadoAsientoRepo = estadoAsientoRepo;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public FuncionResponseDTO crear(FuncionRequestDTO dto) {

        Pelicula pelicula = peliculaRepo.findById(dto.getIdPelicula())
                .orElseThrow(() -> new ResourceNotFoundException("Película no existe"));

        Sala sala = salaRepo.findById(dto.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no existe"));

        // 🔥 VALIDACIÓN DUPLICADO
        funcionRepo.buscarDuplicado(
                dto.getIdSala(),
                dto.getFechaHora()
        ).ifPresent(f -> {
            throw new DuplicateResourceException(
                    "Ya existe una función en esta sala y horario"
            );
        });

        Funcion funcion = new Funcion();
        funcion.setPelicula(pelicula);
        funcion.setSala(sala);
        funcion.setFechaHora(dto.getFechaHora());

        Funcion funcionGuardada = funcionRepo.save(funcion);

        // Crear EstadoAsiento para todos los asientos de la sala en un solo batch
        List<Asiento> asientos = asientoRepo.findBySala_IdSala(sala.getIdSala());
        List<EstadoAsiento> estados = asientos.stream().map(asiento -> {
            EstadoAsiento estado = new EstadoAsiento();
            estado.setFuncion(funcionGuardada);
            estado.setAsiento(asiento);
            estado.setEstado("DISPONIBLE");
            return estado;
        }).collect(Collectors.toList());
        estadoAsientoRepo.saveAll(estados);

        return mapToDTO(funcionGuardada);
    }

    // =========================
    // LIST
    // =========================
    @Override
    public List<FuncionResponseDTO> listar() {
        return funcionRepo.findAllWithJoins()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuncionResponseDTO> listarPorPelicula(Integer idPelicula) {
        return funcionRepo.findByPeliculaIdPelicula(idPelicula)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    public FuncionResponseDTO actualizar(Integer id, FuncionRequestDTO dto) {

        Funcion funcion = funcionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Función no existe"));

        Pelicula pelicula = peliculaRepo.findById(dto.getIdPelicula())
                .orElseThrow(() -> new ResourceNotFoundException("Película no existe"));

        Sala sala = salaRepo.findById(dto.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no existe"));

        // 🔥 VALIDACIÓN DUPLICADO (ignorando el mismo registro)
        funcionRepo.buscarDuplicado(
                dto.getIdSala(),
                dto.getFechaHora()
        ).ifPresent(existing -> {
            if (!existing.getIdFuncion().equals(id)) {
                throw new DuplicateResourceException(
                        "Conflicto: ya existe función en esa sala y horario"
                );
            }
        });

        funcion.setPelicula(pelicula);
        funcion.setSala(sala);
        funcion.setFechaHora(dto.getFechaHora());

        funcionRepo.save(funcion);

        return mapToDTO(funcion);
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void eliminar(Integer id) {
        List<EstadoAsiento> estados = estadoAsientoRepo.findByFuncionIdFuncion(id);
        estadoAsientoRepo.deleteAll(estados);
        
        Funcion funcion = funcionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Función no existe"));

        funcionRepo.delete(funcion);
    }

    @Override
    @Transactional
    public void fixAsientos() {
        List<Funcion> funciones = funcionRepo.findAll();
        for (Funcion funcion : funciones) {
            List<EstadoAsiento> estados = estadoAsientoRepo.findByFuncionIdFuncion(funcion.getIdFuncion());
            if (estados.isEmpty()) {
                List<Asiento> asientos = asientoRepo.findBySala_IdSala(funcion.getSala().getIdSala());
                for (Asiento asiento : asientos) {
                    EstadoAsiento estado = new EstadoAsiento();
                    estado.setFuncion(funcion);
                    estado.setAsiento(asiento);
                    estado.setEstado("DISPONIBLE");
                    estadoAsientoRepo.save(estado);
                }
            }
        }
    }

    // =========================
    // MAPPER
    // =========================
    private FuncionResponseDTO mapToDTO(Funcion f) {

        FuncionResponseDTO dto = new FuncionResponseDTO();

        dto.setIdFuncion(f.getIdFuncion());

        dto.setIdPelicula(f.getPelicula().getIdPelicula());
        dto.setNombrePelicula(f.getPelicula().getNombre());

        dto.setPoster(f.getPelicula().getPoster());

        dto.setIdSala(f.getSala().getIdSala());
        dto.setNumeroSala(f.getSala().getNumeroSala());

        dto.setFechaHora(f.getFechaHora());

        return dto;
    }
}
