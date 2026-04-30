package com.cinemaroyale.service;

import org.springframework.stereotype.Service;
import com.cinemaroyale.dto.GeneroDTO;
import com.cinemaroyale.model.Genero;
import com.cinemaroyale.repository.GeneroRepository;
import com.cinemaroyale.repository.PeliculaGeneroRepository;



import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroServiceImpl implements GeneroService {
   private final GeneroRepository repository;
    private final PeliculaGeneroRepository peliculaGeneroRepository;

    public GeneroServiceImpl(GeneroRepository repository,
                             PeliculaGeneroRepository peliculaGeneroRepository) {
        this.repository = repository;
        this.peliculaGeneroRepository = peliculaGeneroRepository;
    }

    private GeneroDTO mapToDTO(Genero g) {
        return new GeneroDTO(g.getId_genero(), g.getNombre());
    }

    private Genero mapToEntity(GeneroDTO dto) {
        return new Genero(dto.getId_genero(), dto.getNombre());
    }

    @Override
    public List<GeneroDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GeneroDTO getById(Integer id) {
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genero no encontrado"));
        return mapToDTO(genero);
    }

    @Override
    public GeneroDTO save(GeneroDTO dto) {
        Genero genero = repository.save(mapToEntity(dto));
        return mapToDTO(genero);
    }

    @Override
    public GeneroDTO update(Integer id, GeneroDTO dto) {
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genero no encontrado"));

        genero.setNombre(dto.getNombre());

        return mapToDTO(repository.save(genero));
    }

    @Override
    public void delete(Integer id) {

        boolean enUso = peliculaGeneroRepository.existsByGeneroId(id);

        if (enUso) {
            throw new RuntimeException(
                "No se puede eliminar el género porque está asignado a una película"
            );
        }

        repository.deleteById(id);
    }
}