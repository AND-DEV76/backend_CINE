package com.cinemaroyale.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemaroyale.dto.AsientoDTO;
import com.cinemaroyale.service.AsientoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/asientos")
@RequiredArgsConstructor
public class AsientoController {

      private final AsientoService asientoService;

@GetMapping
public List<AsientoDTO> listar() {
    return asientoService.listarDTO();
}

   @GetMapping("/{id}")
public AsientoDTO obtener(@PathVariable Integer id) {
    return asientoService.obtenerDTOPorId(id);
}

@PutMapping("/{id}")
public AsientoDTO actualizar(@PathVariable Integer id, @RequestBody AsientoDTO dto) {
    return asientoService.actualizarDTO(id, dto);
}

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        asientoService.eliminar(id);
    }
    
}
