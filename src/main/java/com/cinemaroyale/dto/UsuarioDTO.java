package com.cinemaroyale.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Integer idUsuario;
    private String username;
    private String nombre;
    private String email;
    private String password; // sin hash aquí
    private Integer idRol;
    private Boolean activo;
    
}
