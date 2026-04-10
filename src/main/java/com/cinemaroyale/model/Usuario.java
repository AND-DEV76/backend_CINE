package com.cinemaroyale.model;



import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

 @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_usuario") //ESTA LÍNEA FALTABA para el imges
private Integer idUsuario;

    @Column(nullable = false, unique = true, name = "username") // 🔥 ESTA LÍNEA ES LA CLAVE para el imges
    private String username;

    @Column(nullable = false, name = "nombre") // 🔥 ESTA LÍNEA ES LA CLAVE para el imges
    private String nombre;

    @Column(nullable = false, unique = true, name = "email") // 🔥 ESTA LÍNEA ES LA CLAVE para el imges
    
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(nullable = false, name = "activo") // 🔥 ESTA LÍNEA ES LA CLAVE para el imges
    private Boolean activo = true;

    @SuppressWarnings("deprecation")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")//necesario para peli
    private Date fechaCreacion = new Date();


    }
