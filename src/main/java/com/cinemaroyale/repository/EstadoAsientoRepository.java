package com.cinemaroyale.repository;

import com.cinemaroyale.model.EstadoAsiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EstadoAsientoRepository extends JpaRepository<EstadoAsiento, Integer> {
    List<EstadoAsiento> findByFuncionIdFuncion(Integer idFuncion);
    Optional<EstadoAsiento> findByFuncionIdFuncionAndAsientoIdAsiento(Integer idFuncion, Integer idAsiento);
}
