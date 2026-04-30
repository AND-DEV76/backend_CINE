package com.cinemaroyale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cinemaroyale.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
  
       @Query("SELECT COUNT(p) > 0 FROM Pelicula p WHERE p.clasificacion.id_clasificacion = :id")
    boolean existsByClasificacionId(@Param("id") Integer id);


}
