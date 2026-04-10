package com.cinemaroyale.service;

import java.util.List;
import com.cinemaroyale.dto.GeneroDTO;

public interface GeneroService {
    List<GeneroDTO> getAll();
    GeneroDTO getById(Integer id);
    GeneroDTO save(GeneroDTO dto);
    GeneroDTO update(Integer id, GeneroDTO dto);
    void delete(Integer id);
}