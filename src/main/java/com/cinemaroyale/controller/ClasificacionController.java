package com.cinemaroyale.controller;


import org.springframework.web.bind.annotation.*;

import com.cinemaroyale.dto.ClasificacionDTO;
import com.cinemaroyale.service.ClasificacionService;

import java.util.List;

@RestController
@RequestMapping("/api/clasificaciones")
@CrossOrigin(origins = "*")
public class ClasificacionController {

     private final ClasificacionService service;

    public ClasificacionController(ClasificacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClasificacionDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ClasificacionDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public ClasificacionDTO create(@RequestBody ClasificacionDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ClasificacionDTO update(@PathVariable Integer id, @RequestBody ClasificacionDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
    
}
