package com.cinemaroyale.service;

import java.util.List;

import com.cinemaroyale.dto.FuncionRequestDTO;
import com.cinemaroyale.dto.FuncionResponseDTO;

public interface FuncionService {
    
    FuncionResponseDTO crear(FuncionRequestDTO dto);

    List<FuncionResponseDTO> listar();

    FuncionResponseDTO actualizar(Integer id, FuncionRequestDTO dto);

    void eliminar(Integer id);

    List<FuncionResponseDTO> listarPorPelicula(Integer idPelicula);

    void fixAsientos();
}
