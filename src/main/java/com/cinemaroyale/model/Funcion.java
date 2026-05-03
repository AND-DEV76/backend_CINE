package com.cinemaroyale.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "Funcion",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_sala", "fecha", "hora"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcion")
    private Integer idFuncion;

    @ManyToOne
    @JoinColumn(name = "id_pelicula", nullable = false)
    private Pelicula pelicula;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;
}
