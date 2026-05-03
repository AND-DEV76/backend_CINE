package com.cinemaroyale.repository;

import com.cinemaroyale.model.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Integer> {

    Optional<Funcion> findBySala_IdSalaAndFechaAndHora(
            Integer idSala,
            LocalDate fecha,
            LocalTime hora
    );

    @Query("""
        SELECT f FROM Funcion f
        WHERE f.sala.idSala = :idSala
        AND f.fecha = :fecha
        AND f.hora = :hora
    """)
    Optional<Funcion> buscarDuplicado(
            @Param("idSala") Integer idSala,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );

    List<Funcion> findByPeliculaIdPelicula(Integer idPelicula);
}
