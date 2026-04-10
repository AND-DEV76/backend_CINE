package com.cinemaroyale.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rol")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rol {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol") //  ESTA LÍNEA ES LA CLAVE para el imges 
    private Integer idRol;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;


    
}
