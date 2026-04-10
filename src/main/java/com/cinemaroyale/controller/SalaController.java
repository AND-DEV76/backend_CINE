package com.cinemaroyale.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemaroyale.dto.SalaDTO;

import com.cinemaroyale.service.SalaService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
public class SalaController {

        private final SalaService salaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody SalaDTO dto) {
        return ResponseEntity.ok(salaService.crearSala(dto));
    }

@GetMapping
public List<SalaDTO> listar() {
    return salaService.listarDTO();
}
   @GetMapping("/{id}")
public SalaDTO obtener(@PathVariable Integer id) {
    return salaService.obtenerDTOPorId(id); 
}

 @PutMapping("/{id}")
public SalaDTO actualizar(@PathVariable Integer id, @RequestBody SalaDTO dto) {
    return salaService.actualizarDTO(id, dto);
}

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        salaService.eliminar(id);
    }
    
}
