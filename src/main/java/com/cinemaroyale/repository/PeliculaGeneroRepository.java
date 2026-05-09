package com.cinemaroyale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cinemaroyale.model.PeliculaGenero;
import com.cinemaroyale.model.PeliculaGeneroId;

import jakarta.transaction.Transactional;

@Repository
public interface PeliculaGeneroRepository  extends JpaRepository<PeliculaGenero, PeliculaGeneroId> {

     
    @Query("SELECT COUNT(pg) > 0 FROM PeliculaGenero pg WHERE pg.genero.id_genero = :id")
    boolean existsByGeneroId(@Param("id") Integer id);


    @Modifying
@Transactional
@Query("DELETE FROM PeliculaGenero pg WHERE pg.pelicula.idPelicula = :id")
void deleteByPeliculaId(@Param("id") Integer id);


@Query("SELECT pg.genero.nombre FROM PeliculaGenero pg WHERE pg.pelicula.idPelicula = :id")
List<String> findGenerosByPeliculaId(@Param("id") Integer id);

@Query("SELECT pg.genero.id_genero FROM PeliculaGenero pg WHERE pg.pelicula.idPelicula = :id")
List<Integer> findGeneroIdsByPeliculaId(@Param("id") Integer id);


}