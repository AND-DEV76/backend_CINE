package com.cinemaroyale.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MetodoPago")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
}
