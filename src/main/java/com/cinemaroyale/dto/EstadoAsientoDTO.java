package com.cinemaroyale.dto;

import lombok.Data;

@Data
public class EstadoAsientoDTO {
    private Integer idEstado;
    private Integer idFuncion;
    private Integer idAsiento;
    private String fila;
    private Integer numero;
    private String estado;
}
