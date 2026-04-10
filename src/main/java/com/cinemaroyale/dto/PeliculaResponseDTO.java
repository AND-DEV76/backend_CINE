package com.cinemaroyale.dto;

import lombok.Data;

@Data
public class PeliculaResponseDTO {


    private Integer idPelicula;
    private String nombre;
    private Integer duracion;
    private String clasificacion;
    private String descripcion;
    private String poster;
    private String creadoPor;
    
}
