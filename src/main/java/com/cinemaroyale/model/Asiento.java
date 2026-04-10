package com.cinemaroyale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Asiento", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_sala", "fila", "numero"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Asiento {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento") //  ESTA LÍNEA ES LA CLAVE para el imges
    @JsonIgnore
    private Integer idAsiento;

    @ManyToOne
    @JoinColumn(name = "id_sala")
    private Sala sala;

    @Column(name = "fila") //  ESTA LÍNEA ES LA CLAVE para el imges
    private String fila;

    @Column(name = "numero") //  ESTA LÍNEA ES LA CLAVE para el imges
    private Integer numero;
}
