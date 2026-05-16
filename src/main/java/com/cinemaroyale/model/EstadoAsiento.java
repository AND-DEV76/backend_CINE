package com.cinemaroyale.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "EstadoAsiento")
public class EstadoAsiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @ManyToOne
    @JoinColumn(name = "id_funcion", nullable = false)
    private Funcion funcion;

    @ManyToOne
    @JoinColumn(name = "id_asiento", nullable = false)
    private Asiento asiento;

    @Column(nullable = false)
    private String estado = "DISPONIBLE"; // DISPONIBLE, RESERVADO, OCUPADO

    @Column(name = "reservado_hasta")
    private java.time.LocalDateTime reservadoHasta;

    @Column(name = "reservado_por")
    private Integer reservadoPor;
}
