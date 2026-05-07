package com.cinemaroyale.dto;

import java.time.LocalDateTime;
import lombok.Data;
@Data
public class FuncionRequestDTO {
    
    private Integer idPelicula;
    private Integer idSala;
    private LocalDateTime fechaHora;
}
