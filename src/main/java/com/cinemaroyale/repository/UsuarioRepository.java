package com.cinemaroyale.repository;


import com.cinemaroyale.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
  Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

}
