package com.cinemaroyale.repository;

import com.cinemaroyale.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
    List<Boleto> findByCompraIdCompra(Integer idCompra);
}
