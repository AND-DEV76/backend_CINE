package com.cinemaroyale.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PeliculaGenero")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaGenero {

    @EmbeddedId
    private PeliculaGeneroId id;

    @ManyToOne
    @MapsId("id_pelicula")
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    @ManyToOne
    @MapsId("id_genero")
    @JoinColumn(name = "id_genero")
    private Genero genero;
    
}
