package com.cinemaroyale.repository;

import com.cinemaroyale.model.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Integer> {

    Optional<Funcion> findBySala_IdSalaAndFechaHora(
            Integer idSala,
            LocalDateTime fechaHora
    );

    @Query("""
        SELECT f FROM Funcion f
        WHERE f.sala.idSala = :idSala
        AND f.fechaHora = :fechaHora
    """)
    Optional<Funcion> buscarDuplicado(
            @Param("idSala") Integer idSala,
            @Param("fechaHora") LocalDateTime fechaHora
    );

    List<Funcion> findByPeliculaIdPelicula(Integer idPelicula);
}
