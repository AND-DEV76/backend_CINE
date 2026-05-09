package com.cinemaroyale.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.cinemaroyale.dto.PeliculaRequestDTO;
import com.cinemaroyale.dto.PeliculaResponseDTO;
import com.cinemaroyale.service.PeliculaService;

import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
@CrossOrigin("*")

public class PeliculaController {

      @Autowired
    private PeliculaService peliculaService;

    // CREAR CON IMAGEN
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PeliculaResponseDTO crear(@ModelAttribute PeliculaRequestDTO dto) {
        return peliculaService.crear(dto);
    }

    @GetMapping
    public List<PeliculaResponseDTO> listar() {
        return peliculaService.listar();
    }

    @GetMapping("/{id}")
    public PeliculaResponseDTO obtener(@PathVariable Integer id) {
        return peliculaService.obtenerPorId(id);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PeliculaResponseDTO actualizar(
            @PathVariable Integer id,
            @ModelAttribute PeliculaRequestDTO dto) {

        return peliculaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        peliculaService.eliminar(id);
    }


    
}
