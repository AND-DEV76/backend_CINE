package com.cinemaroyale.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


import lombok.Data;


@Data
public class FuncionResponseDTO {
    
      private Integer idFuncion;
    private Integer idPelicula;
    private String nombrePelicula;

    private Integer idSala;
    private Integer numeroSala;

    private LocalDate fecha;
    private LocalDateTime hora;

    private String poster; 
}
