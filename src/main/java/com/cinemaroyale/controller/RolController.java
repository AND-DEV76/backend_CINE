package com.cinemaroyale.controller;


import org.springframework.web.bind.annotation.*;

import com.cinemaroyale.model.Rol;
import com.cinemaroyale.service.RolService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin

public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listar() {
        return rolService.listar();
    }

    @PostMapping
    public Rol crear(@RequestBody Rol rol) {
        return rolService.guardar(rol);
    }

    @GetMapping("/{id}")
    public Rol obtener(@PathVariable Integer id) {
        return rolService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public Rol actualizar(@PathVariable Integer id, @RequestBody Rol rol) {
        return rolService.actualizar(id, rol);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        rolService.eliminar(id);
    }


    
    
}
