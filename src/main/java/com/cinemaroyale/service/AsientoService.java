package com.cinemaroyale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinemaroyale.dto.AsientoDTO;
import com.cinemaroyale.exceptions.ResourceNotFoundException;
import com.cinemaroyale.model.Asiento;
import com.cinemaroyale.repository.AsientoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsientoService {

    private final AsientoRepository asientoRepository;

public List<AsientoDTO> listarDTO() {
    return asientoRepository.findAll().stream().map(a -> {
        AsientoDTO dto = new AsientoDTO();
        dto.setIdAsiento(a.getIdAsiento());
        dto.setFila(a.getFila());
        dto.setNumero(a.getNumero());
        return dto;
    }).toList();
}
    public Asiento obtenerPorId(Integer id) {
        return asientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asiento no encontrado"));
    }

public AsientoDTO actualizarDTO(Integer id, AsientoDTO dto) {

    Asiento asiento = asientoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asiento no encontrado"));

    // ✅ UPDATE PARCIAL
    if (dto.getFila() != null) {
        asiento.setFila(dto.getFila());
    }

    if (dto.getNumero() != null) {
        asiento.setNumero(dto.getNumero());
    }

    Asiento actualizado = asientoRepository.save(asiento);

    // ✅ convertir a DTO (sin sala → sin bucle)
    AsientoDTO response = new AsientoDTO();
    response.setIdAsiento(actualizado.getIdAsiento());
    response.setFila(actualizado.getFila());
    response.setNumero(actualizado.getNumero());

    return response;
}

    public void eliminar(Integer id) {
        Asiento asiento = obtenerPorId(id);
        asientoRepository.delete(asiento);
    }
    

    public AsientoDTO obtenerDTOPorId(Integer id) {

    Asiento asiento = asientoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asiento no encontrado"));

    AsientoDTO dto = new AsientoDTO();
    dto.setIdAsiento(asiento.getIdAsiento());
    dto.setFila(asiento.getFila());
    dto.setNumero(asiento.getNumero());

    return dto;
}
}
