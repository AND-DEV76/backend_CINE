package com.cinemaroyale.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FuncionDTO {
    private Integer idFuncion;
    private Integer idPelicula;
    private String nombrePelicula;
    private Integer idSala;
    private Integer numeroSala;
    private LocalDateTime fechaHora;
}
