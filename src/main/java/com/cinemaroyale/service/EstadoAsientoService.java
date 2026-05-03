package com.cinemaroyale.service;

import com.cinemaroyale.dto.EstadoAsientoDTO;
import java.util.List;

public interface EstadoAsientoService {
    List<EstadoAsientoDTO> listarPorFuncion(Integer idFuncion);
}
