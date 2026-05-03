package com.cinemaroyale.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.cinemaroyale.dto.FuncionRequestDTO;
import com.cinemaroyale.dto.FuncionResponseDTO;
import com.cinemaroyale.dto.EstadoAsientoDTO;
import com.cinemaroyale.service.FuncionService;
import com.cinemaroyale.service.EstadoAsientoService;


@RestController
@RequestMapping("/api/funciones")
@CrossOrigin(origins = "*")
public class FuncionController {

    private final FuncionService service;
    private final EstadoAsientoService estadoAsientoService;

    public FuncionController(FuncionService service, EstadoAsientoService estadoAsientoService) {
        this.service = service;
        this.estadoAsientoService = estadoAsientoService;
    }

    @PostMapping
    public FuncionResponseDTO crear(@RequestBody FuncionRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping
    public List<FuncionResponseDTO> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public FuncionResponseDTO actualizar(
            @PathVariable Integer id,
            @RequestBody FuncionRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }

    @GetMapping("/pelicula/{idPelicula}")
    public ResponseEntity<List<FuncionResponseDTO>> listarPorPelicula(@PathVariable Integer idPelicula) {
        return ResponseEntity.ok(service.listarPorPelicula(idPelicula));
    }

    @GetMapping("/{id}/asientos")
    public ResponseEntity<List<EstadoAsientoDTO>> obtenerAsientos(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoAsientoService.listarPorFuncion(id));
    }

    // 🔥 Endpoint temporal para arreglar asientos de funciones creadas manualmente sin asientos
    @GetMapping("/fix-asientos")
    public ResponseEntity<String> fixAsientos() {
        service.fixAsientos();
        return ResponseEntity.ok("Asientos arreglados");
    }
}
