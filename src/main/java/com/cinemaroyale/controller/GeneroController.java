package com.cinemaroyale.controller;


import org.springframework.web.bind.annotation.*;

import com.cinemaroyale.dto.GeneroDTO;
import com.cinemaroyale.service.GeneroService;

import java.util.List;

@RestController
@RequestMapping("/api/generos")
@CrossOrigin(origins = "*")

public class GeneroController {
    

     private final GeneroService service;

    public GeneroController(GeneroService service) {
        this.service = service;
    }

    @GetMapping
    public List<GeneroDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GeneroDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public GeneroDTO create(@RequestBody GeneroDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public GeneroDTO update(@PathVariable Integer id, @RequestBody GeneroDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
