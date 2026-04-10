package com.cinemaroyale.dto;


import lombok.Data;

import org.springframework.web.multipart.MultipartFile;
@Data
public class PeliculaRequestDTO {

     private String nombre;
    private Integer duracion;
    private Integer idClasificacion;
    private String descripcion;
    private MultipartFile poster; // aquí viene la imagen
    private Integer creadoPor;
    
}
