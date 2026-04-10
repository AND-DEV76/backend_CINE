package com.cinemaroyale.controller;


import org.springframework.web.bind.annotation.*;

import com.cinemaroyale.dto.UsuarioDTO;
import com.cinemaroyale.model.Usuario;
import com.cinemaroyale.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    public Usuario crear(@RequestBody UsuarioDTO dto) {
        return usuarioService.guardar(dto);
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Integer id, @RequestBody UsuarioDTO dto) {
        return usuarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
    }
    
}
