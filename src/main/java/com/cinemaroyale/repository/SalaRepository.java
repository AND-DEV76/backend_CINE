package com.cinemaroyale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinemaroyale.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
     
}
