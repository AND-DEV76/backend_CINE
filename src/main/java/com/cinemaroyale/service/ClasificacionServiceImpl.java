package com.cinemaroyale.service;

import org.springframework.stereotype.Service;
import com.cinemaroyale.dto.ClasificacionDTO;
import com.cinemaroyale.model.Clasificacion;
import com.cinemaroyale.repository.ClasificacionRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClasificacionServiceImpl implements ClasificacionService {

    private final ClasificacionRepository repository;

    public ClasificacionServiceImpl(ClasificacionRepository repository) {
        this.repository = repository;
    }

    private ClasificacionDTO mapToDTO(Clasificacion c) {
        return new ClasificacionDTO(
                c.getId_clasificacion(),
                c.getNombre(),
                c.getDescripcion()
        );
    }

    private Clasificacion mapToEntity(ClasificacionDTO dto) {
        return new Clasificacion(
                dto.getId_clasificacion(),
                dto.getNombre(),
                dto.getDescripcion()
        );
    }

    @Override
    public List<ClasificacionDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClasificacionDTO getById(Integer id) {
        Clasificacion c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada"));
        return mapToDTO(c);
    }

    @Override
    public ClasificacionDTO save(ClasificacionDTO dto) {
        Clasificacion c = repository.save(mapToEntity(dto));
        return mapToDTO(c);
    }

    @Override
    public ClasificacionDTO update(Integer id, ClasificacionDTO dto) {
        Clasificacion c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada"));

        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());

        return mapToDTO(repository.save(c));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}