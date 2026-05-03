package com.cinemaroyale.service;

import com.cinemaroyale.dto.BoletoDTO;
import com.cinemaroyale.dto.CompraDTO;
import java.util.List;

public interface CompraService {
    List<BoletoDTO> procesarCompra(CompraDTO dto);
    List<BoletoDTO> obtenerBoletosPorUsuario(Integer idUsuario);
}
