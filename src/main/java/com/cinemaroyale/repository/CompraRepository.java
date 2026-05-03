package com.cinemaroyale.repository;

import com.cinemaroyale.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByClienteIdCliente(Integer idCliente);
}
