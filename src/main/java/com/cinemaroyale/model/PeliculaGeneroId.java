package com.cinemaroyale.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor


public class PeliculaGeneroId  implements Serializable{
    
    private Integer id_pelicula;
    private Integer id_genero;

    // Constructor, getters, setters, equals y hashCode
    
}
