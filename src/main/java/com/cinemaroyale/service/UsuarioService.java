package com.cinemaroyale.service;

import org.springframework.stereotype.Service;

import com.cinemaroyale.dto.UsuarioDTO;
import com.cinemaroyale.model.Rol;
import com.cinemaroyale.model.Usuario;
import com.cinemaroyale.repository.RolRepository;
import com.cinemaroyale.repository.UsuarioRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;

@Service

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    // 🔐 HASH SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar password");
        }
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario guardar(UsuarioDTO dto) {
        Rol rol = rolRepository.findById(dto.getIdRol()).orElseThrow();

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPasswordHash(hashPassword(dto.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    public Usuario actualizar(Integer id, UsuarioDTO dto) {
        Usuario usuario = obtenerPorId(id);

        usuario.setUsername(dto.getUsername());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());

        if (dto.getPassword() != null) {
            usuario.setPasswordHash(hashPassword(dto.getPassword()));
        }

        if (dto.getIdRol() != null) {
            Rol rol = rolRepository.findById(dto.getIdRol()).orElseThrow();
            usuario.setRol(rol);
        }

        usuario.setActivo(dto.getActivo());

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
    
}
