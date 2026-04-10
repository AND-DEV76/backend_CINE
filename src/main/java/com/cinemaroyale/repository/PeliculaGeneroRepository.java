package com.cinemaroyale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinemaroyale.model.PeliculaGenero;
import com.cinemaroyale.model.PeliculaGeneroId;

@Repository
public interface PeliculaGeneroRepository  extends JpaRepository<PeliculaGenero, PeliculaGeneroId> {
}