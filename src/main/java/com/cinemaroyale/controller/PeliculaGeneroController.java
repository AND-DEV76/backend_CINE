package com.cinemaroyale.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinemaroyale.dto.PeliculaGeneroDTO;
import com.cinemaroyale.service.PeliculaGeneroService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pelicula-genero")
@RequiredArgsConstructor
public class PeliculaGeneroController {


    private final PeliculaGeneroService service;

    // CREAR RELACIÓN
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PeliculaGeneroDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    // LISTAR
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // ELIMINAR
    @DeleteMapping
    public ResponseEntity<?> eliminar(
            @RequestParam Integer idPelicula,
            @RequestParam Integer idGenero) {

        service.eliminar(idPelicula, idGenero);
        return ResponseEntity.ok("Relación eliminada");
    }


    
}
