package com.cinemaroyale.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
@Data
public class FuncionRequestDTO {
    

    private Integer idPelicula;
    private Integer idSala;
    private LocalDate fecha;   // formato: yyyy-MM-dd
    private LocalTime hora;    // formato: HH:mm:ss
}
