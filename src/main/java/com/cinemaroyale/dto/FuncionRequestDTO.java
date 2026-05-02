package com.cinemaroyale.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class FuncionRequestDTO {
    

    private Integer idPelicula;
    private Integer idSala;
    private LocalDate fecha;   // formato: yyyy-MM-dd
    private LocalDateTime hora;    // formato: HH:mm:ss
}
