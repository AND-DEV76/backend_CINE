package com.cinemaroyale.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Pelicula")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula") //  ESTA LÍNEA ES LA CLAVE para el imges
    private Integer idPelicula;

    @Column(nullable = false, length = 150, name = "nombre") //  ESTA LÍNEA ES LA CLAVE para el imges
    private String nombre;

    @Column(nullable = false, name = "duracion") //  ESTA LÍNEA ES LA CLAVE para el imges
    private Integer duracion;

    @ManyToOne
    @JoinColumn(name = "id_clasificacion", nullable = false)
    private Clasificacion clasificacion;

    @Column(columnDefinition = "VARCHAR(MAX)", name = "descripcion") // ESTA LÍNEA ES LA CLAVE para el imges
    private String descripcion;

    @Column(name = "poster") //  ESTA LÍNEA ES LA CLAVE para el imges
    private String poster; // nombre de la imagen

    @ManyToOne
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;


    
}
