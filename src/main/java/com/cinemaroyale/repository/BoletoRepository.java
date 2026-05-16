package com.cinemaroyale.repository;

import com.cinemaroyale.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
    List<Boleto> findByCompraIdCompra(Integer idCompra);

    // Obtiene todos los boletos de un usuario en una sola consulta con JOIN
    @Query("""
        SELECT b FROM Boleto b
        JOIN FETCH b.compra c
        JOIN FETCH c.cliente cl
        JOIN FETCH b.funcion f
        JOIN FETCH f.pelicula
        JOIN FETCH f.sala
        JOIN FETCH b.asiento
        WHERE cl.usuario.idUsuario = :idUsuario
    """)
    List<Boleto> findAllByUsuarioId(@Param("idUsuario") Integer idUsuario);
}
