package com.cinemaroyale.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinemaroyale.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    
}
