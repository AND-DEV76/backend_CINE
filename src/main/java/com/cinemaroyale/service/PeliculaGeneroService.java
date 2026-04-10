package com.cinemaroyale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinemaroyale.dto.PeliculaGeneroDTO;
import com.cinemaroyale.model.Genero;
import com.cinemaroyale.model.Pelicula;
import com.cinemaroyale.model.PeliculaGenero;
import com.cinemaroyale.model.PeliculaGeneroId;
import com.cinemaroyale.repository.GeneroRepository;
import com.cinemaroyale.repository.PeliculaGeneroRepository;
import com.cinemaroyale.repository.PeliculaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeliculaGeneroService {

     private final PeliculaGeneroRepository repository;
    private final PeliculaRepository peliculaRepository;
    private final GeneroRepository generoRepository;

    // CREAR RELACIÓN
    public PeliculaGenero crear(PeliculaGeneroDTO dto) {

        Pelicula pelicula = peliculaRepository.findById(dto.getIdPelicula())
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        Genero genero = generoRepository.findById(dto.getIdGenero())
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));

        PeliculaGeneroId id = new PeliculaGeneroId(
                dto.getIdPelicula(),
                dto.getIdGenero()
        );

        PeliculaGenero relacion = new PeliculaGenero();
        relacion.setId(id);
        relacion.setPelicula(pelicula);
        relacion.setGenero(genero);

        return repository.save(relacion);
    }

    // LISTAR TODO
    public List<PeliculaGenero> listar() {
        return repository.findAll();
    }

    // ELIMINAR RELACIÓN
    public void eliminar(Integer idPelicula, Integer idGenero) {
        PeliculaGeneroId id = new PeliculaGeneroId(idPelicula, idGenero);
        repository.deleteById(id);
    }
    
}
