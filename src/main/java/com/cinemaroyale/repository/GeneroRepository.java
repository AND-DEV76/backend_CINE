package com.cinemaroyale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cinemaroyale.model.Genero;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    Optional<Genero> findByNombre(String nombre);
}