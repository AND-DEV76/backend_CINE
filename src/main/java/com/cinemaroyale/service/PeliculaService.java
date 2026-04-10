package com.cinemaroyale.service;

import com.cinemaroyale.dto.PeliculaRequestDTO;
import com.cinemaroyale.dto.PeliculaResponseDTO;
import java.util.List;

public interface PeliculaService {
    
    PeliculaResponseDTO crear(PeliculaRequestDTO dto);

    List<PeliculaResponseDTO> listar();

    PeliculaResponseDTO obtenerPorId(Integer id);

    PeliculaResponseDTO actualizar(Integer id, PeliculaRequestDTO dto);

    void eliminar(Integer id);
}
