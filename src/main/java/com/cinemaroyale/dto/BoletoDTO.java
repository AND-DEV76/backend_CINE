package com.cinemaroyale.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BoletoDTO {
    private Integer idBoleto;
    private String nombrePelicula;
    private Integer numeroSala;
    private LocalDate fecha;
    private LocalTime hora;
    private String fila;
    private Integer numeroAsiento;
    private BigDecimal precio;
    private String estado;
}
