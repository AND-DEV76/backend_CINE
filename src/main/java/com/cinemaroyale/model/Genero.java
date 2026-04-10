package com.cinemaroyale.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Genero")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genero {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_genero;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
    
}
