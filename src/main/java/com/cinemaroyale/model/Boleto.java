package com.cinemaroyale.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Boleto")
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleto")
    private Integer idBoleto;

    @ManyToOne
    @JoinColumn(name = "id_funcion", nullable = false)
    private Funcion funcion;

    @ManyToOne
    @JoinColumn(name = "id_asiento", nullable = false)
    private Asiento asiento;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private String estado = "ACTIVO";
}
