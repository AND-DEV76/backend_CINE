package com.cinemaroyale.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Clasificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clasificacion {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_clasificacion;

    @Column(nullable = false, length = 10)
    private String nombre;

    @Column(length = 100)
    private String descripcion;
    
}
