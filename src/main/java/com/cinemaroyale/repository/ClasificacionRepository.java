package com.cinemaroyale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cinemaroyale.model.Clasificacion;

public interface ClasificacionRepository extends JpaRepository<Clasificacion, Integer> {
}