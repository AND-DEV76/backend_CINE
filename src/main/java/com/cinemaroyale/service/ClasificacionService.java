package com.cinemaroyale.service;

import java.util.List;
import com.cinemaroyale.dto.ClasificacionDTO;

public interface ClasificacionService {
    List<ClasificacionDTO> getAll();
    ClasificacionDTO getById(Integer id);
    ClasificacionDTO save(ClasificacionDTO dto);
    ClasificacionDTO update(Integer id, ClasificacionDTO dto);
    void delete(Integer id);
}