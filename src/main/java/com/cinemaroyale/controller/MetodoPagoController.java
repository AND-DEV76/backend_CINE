package com.cinemaroyale.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinemaroyale.dto.MetodoPagoDTO;
import com.cinemaroyale.service.MetodoPagoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/metodos-pago")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para React
public class MetodoPagoController {

    private final MetodoPagoService service;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST
    @PostMapping
    public ResponseEntity<MetodoPagoDTO> crear(@RequestBody MetodoPagoDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody MetodoPagoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
