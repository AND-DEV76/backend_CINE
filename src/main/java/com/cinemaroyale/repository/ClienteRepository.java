package com.cinemaroyale.repository;

import com.cinemaroyale.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByUsuarioIdUsuario(Integer idUsuario);
}
