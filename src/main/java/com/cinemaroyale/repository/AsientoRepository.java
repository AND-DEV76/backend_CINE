package com.cinemaroyale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinemaroyale.model.Asiento;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Integer> {

    List<Asiento> findBySala_IdSala(Integer idSala);
}