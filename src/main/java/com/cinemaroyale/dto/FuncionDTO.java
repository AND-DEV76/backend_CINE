package com.cinemaroyale.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FuncionDTO {
    private Integer idFuncion;
    private Integer idPelicula;
    private String nombrePelicula;
    private Integer idSala;
    private Integer numeroSala;
    private LocalDate fecha;
    private LocalTime hora;
}
