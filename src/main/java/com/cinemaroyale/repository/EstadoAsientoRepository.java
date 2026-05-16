package com.cinemaroyale.repository;

import com.cinemaroyale.model.EstadoAsiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EstadoAsientoRepository extends JpaRepository<EstadoAsiento, Integer> {
    List<EstadoAsiento> findByFuncionIdFuncion(Integer idFuncion);
    Optional<EstadoAsiento> findByFuncionIdFuncionAndAsientoIdAsiento(Integer idFuncion, Integer idAsiento);

    // Bloqueo pesimista para evitar doble-booking concurrente
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EstadoAsiento e WHERE e.funcion.idFuncion = :idFuncion AND e.asiento.idAsiento = :idAsiento")
    Optional<EstadoAsiento> findForUpdate(@Param("idFuncion") Integer idFuncion, @Param("idAsiento") Integer idAsiento);

    // Reservas expiradas que deben liberarse
    @Query("SELECT e FROM EstadoAsiento e WHERE e.estado = 'RESERVADO' AND e.reservadoHasta < :now")
    List<EstadoAsiento> findExpiredReservations(@Param("now") LocalDateTime now);

    // Reservas de un usuario en una funcion (para liberar al salir)
    @Query("SELECT e FROM EstadoAsiento e WHERE e.funcion.idFuncion = :idFuncion AND e.reservadoPor = :idUsuario AND e.estado = 'RESERVADO'")
    List<EstadoAsiento> findByFuncionAndReservadoPor(@Param("idFuncion") Integer idFuncion, @Param("idUsuario") Integer idUsuario);
}
