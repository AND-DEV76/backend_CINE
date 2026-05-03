package com.cinemaroyale.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompraDTO {
    private Integer idUsuario;
    private Integer idMetodoPago;
    private List<Integer> idAsientos;
    private Integer idFuncion;
    private BigDecimal total;
}
