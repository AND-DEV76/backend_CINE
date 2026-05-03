package com.cinemaroyale.service;

import com.cinemaroyale.dto.EstadoAsientoDTO;
import com.cinemaroyale.model.EstadoAsiento;
import com.cinemaroyale.repository.EstadoAsientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoAsientoServiceImpl implements EstadoAsientoService {

    @Autowired
    private EstadoAsientoRepository estadoAsientoRepository;

    @Override
    public List<EstadoAsientoDTO> listarPorFuncion(Integer idFuncion) {
        return estadoAsientoRepository.findByFuncionIdFuncion(idFuncion)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private EstadoAsientoDTO mapToDTO(EstadoAsiento e) {
        EstadoAsientoDTO dto = new EstadoAsientoDTO();
        dto.setIdEstado(e.getIdEstado());
        dto.setIdFuncion(e.getFuncion().getIdFuncion());
        dto.setIdAsiento(e.getAsiento().getIdAsiento());
        dto.setFila(e.getAsiento().getFila());
        dto.setNumero(e.getAsiento().getNumero());
        dto.setEstado(e.getEstado());
        return dto;
    }
}
