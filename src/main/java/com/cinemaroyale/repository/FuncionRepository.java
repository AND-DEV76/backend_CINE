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

    @Query("""
        SELECT f FROM Funcion f
        WHERE f.sala.idSala = :idSala
        AND f.fechaHora = :fechaHora
    """)
    Optional<Funcion> buscarDuplicado(
            @Param("idSala") Integer idSala,
            @Param("fechaHora") LocalDateTime fechaHora
    );

    // Una sola consulta con JOIN para evitar N+1 al listar por pelicula
    @Query("""
        SELECT f FROM Funcion f
        JOIN FETCH f.pelicula p
        JOIN FETCH f.sala s
        WHERE f.pelicula.idPelicula = :idPelicula
    """)
    List<Funcion> findByPeliculaIdPelicula(@Param("idPelicula") Integer idPelicula);

    // Una sola consulta con JOIN para listar todas las funciones
    @Query("""
        SELECT f FROM Funcion f
        JOIN FETCH f.pelicula p
        JOIN FETCH f.sala s
    """)
    List<Funcion> findAllWithJoins();
}
