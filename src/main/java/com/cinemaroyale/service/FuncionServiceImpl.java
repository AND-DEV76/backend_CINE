package com.cinemaroyale.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cinemaroyale.dto.FuncionRequestDTO;
import com.cinemaroyale.dto.FuncionResponseDTO;
import com.cinemaroyale.exceptions.ResourceNotFoundException;
import com.cinemaroyale.model.Funcion;
import com.cinemaroyale.model.Pelicula;
import com.cinemaroyale.model.Sala;
import com.cinemaroyale.repository.FuncionRepository;
import com.cinemaroyale.repository.PeliculaRepository;
import com.cinemaroyale.repository.SalaRepository;
import com.cinemaroyale.exceptions.DuplicateResourceException;
import java.util.List;

@Service
public class FuncionServiceImpl implements FuncionService {

   
    private final FuncionRepository funcionRepo;
    private final PeliculaRepository peliculaRepo;
    private final SalaRepository salaRepo;

    public FuncionServiceImpl(FuncionRepository funcionRepo,
                              PeliculaRepository peliculaRepo,
                              SalaRepository salaRepo) {
        this.funcionRepo = funcionRepo;
        this.peliculaRepo = peliculaRepo;
        this.salaRepo = salaRepo;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    public FuncionResponseDTO crear(FuncionRequestDTO dto) {

        Pelicula pelicula = peliculaRepo.findById(dto.getIdPelicula())
                .orElseThrow(() -> new ResourceNotFoundException("Película no existe"));

        Sala sala = salaRepo.findById(dto.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no existe"));

        // 🔥 VALIDACIÓN DUPLICADO
        funcionRepo.buscarDuplicado(
                dto.getIdSala(),
                dto.getFecha(),
                dto.getHora()
        ).ifPresent(f -> {
            throw new DuplicateResourceException(
                    "Ya existe una función en esta sala, fecha y hora"
            );
        });

        Funcion funcion = new Funcion();
        funcion.setPelicula(pelicula);
        funcion.setSala(sala);
        funcion.setFecha(dto.getFecha());
        funcion.setHora(dto.getHora());

        funcionRepo.save(funcion);

        return mapToDTO(funcion);
    }

    // =========================
    // LIST
    // =========================
    @Override
    public List<FuncionResponseDTO> listar() {
        return funcionRepo.findAll()
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
                dto.getFecha(),
                dto.getHora()
        ).ifPresent(existing -> {
            if (!existing.getIdFuncion().equals(id)) {
                throw new DuplicateResourceException(
                        "Conflicto: ya existe función en esa sala/fecha/hora"
                );
            }
        });

        funcion.setPelicula(pelicula);
        funcion.setSala(sala);
        funcion.setFecha(dto.getFecha());
        funcion.setHora(dto.getHora());

        funcionRepo.save(funcion);

        return mapToDTO(funcion);
    }

    // =========================
    // DELETE
    // =========================
    @Override
    public void eliminar(Integer id) {

        Funcion funcion = funcionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Función no existe"));

        funcionRepo.delete(funcion);
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

        dto.setFecha(f.getFecha());
        dto.setHora(f.getHora());

        return dto;
    }    
}
