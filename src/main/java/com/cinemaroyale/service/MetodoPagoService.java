package com.cinemaroyale.service;

import java.util.List;
import com.cinemaroyale.dto.MetodoPagoDTO;

public interface MetodoPagoService {

    List<MetodoPagoDTO> listar();

    MetodoPagoDTO obtenerPorId(Integer id);

    MetodoPagoDTO guardar(MetodoPagoDTO dto);

    MetodoPagoDTO actualizar(Integer id, MetodoPagoDTO dto);

    void eliminar(Integer id);
}