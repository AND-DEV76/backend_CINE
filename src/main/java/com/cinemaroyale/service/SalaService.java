package com.cinemaroyale.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cinemaroyale.dto.SalaDTO;
import com.cinemaroyale.exceptions.BadRequestException;
import com.cinemaroyale.exceptions.ResourceNotFoundException;
import com.cinemaroyale.model.Asiento;
import com.cinemaroyale.model.Sala;
import com.cinemaroyale.repository.AsientoRepository;
import com.cinemaroyale.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class SalaService {
    private final SalaRepository salaRepository;
    private final AsientoRepository asientoRepository;

    public Sala crearSala(SalaDTO dto) {

        // 🔥 VALIDACIÓN
        if (dto.getCapacidad() > 16) {
            throw new BadRequestException("Nuestra sala no cuenta con mayor capacidad");
        }

        Sala sala = new Sala();
        sala.setNumeroSala(dto.getNumeroSala());
        sala.setCapacidad(dto.getCapacidad());

        Sala salaGuardada = salaRepository.save(sala);

        // 🎬 GENERAR ASIENTOS AUTOMÁTICOS (4x4)
        generarAsientos(salaGuardada);

        return salaGuardada;
    }

    private void generarAsientos(Sala sala) {

        char[] filas = {'A', 'B', 'C', 'D'};
        int columnas = 4;

        List<Asiento> lista = new ArrayList<>();

        for (char fila : filas) {
            for (int i = 1; i <= columnas; i++) {

                Asiento asiento = new Asiento();
                asiento.setSala(sala);
                asiento.setFila(String.valueOf(fila));
                asiento.setNumero(i);

                lista.add(asiento);
            }
        }

        asientoRepository.saveAll(lista);
    }


public List<SalaDTO> listarDTO() {
    return salaRepository.findAll().stream().map(sala -> {
        SalaDTO dto = new SalaDTO();
        dto.setIdSala(sala.getIdSala());
        dto.setNumeroSala(sala.getNumeroSala());
        dto.setCapacidad(sala.getCapacidad());
        return dto;
    }).collect(Collectors.toList());
}


public SalaDTO obtenerDTOPorId(Integer id) {

    Sala sala = salaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));

    SalaDTO dto = new SalaDTO();
    dto.setIdSala(sala.getIdSala());
    dto.setNumeroSala(sala.getNumeroSala());
    dto.setCapacidad(sala.getCapacidad());

    return dto;
}


public Sala obtenerPorId(Integer id) {
    return salaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));
}
    public Sala actualizar(Integer id, SalaDTO dto) {

        Sala sala = obtenerPorId(id);

        if (dto.getCapacidad() > 16) {
            throw new BadRequestException("Capacidad inválida");
        }

        sala.setNumeroSala(dto.getNumeroSala());
        sala.setCapacidad(dto.getCapacidad());

        return salaRepository.save(sala);
    }

    public void eliminar(Integer id) {
        Sala sala = obtenerPorId(id);
        salaRepository.delete(sala);
    }



    public SalaDTO actualizarDTO(Integer id, SalaDTO dto) {

    Sala sala = obtenerPorId(id);

    if (dto.getCapacidad() != null && dto.getCapacidad() > 16) {
        throw new BadRequestException("Capacidad inválida");
    }

    // actualización parcial (clave 🔥)
    if (dto.getNumeroSala() != null) {
        sala.setNumeroSala(dto.getNumeroSala());
    }

    if (dto.getCapacidad() != null) {
        sala.setCapacidad(dto.getCapacidad());
    }

    Sala actualizada = salaRepository.save(sala);

    // convertir a DTO
    SalaDTO response = new SalaDTO();
    response.setIdSala(actualizada.getIdSala());
    response.setNumeroSala(actualizada.getNumeroSala());
    response.setCapacidad(actualizada.getCapacidad());

    return response;
}
}
