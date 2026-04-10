package com.cinemaroyale.service;


import org.springframework.stereotype.Service;

import com.cinemaroyale.model.Rol;
import com.cinemaroyale.repository.RolRepository;



import java.util.List;

@Service

public class RolService {


    private RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol obtenerPorId(Integer id) {
        return rolRepository.findById(id).orElseThrow();
    }

    public Rol actualizar(Integer id, Rol rol) {
        Rol existente = obtenerPorId(id);
        existente.setNombre(rol.getNombre());
        return rolRepository.save(existente);
    }

    public void eliminar(Integer id) {
        rolRepository.deleteById(id);
    }
    
}
