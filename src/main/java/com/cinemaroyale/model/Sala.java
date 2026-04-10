package com.cinemaroyale.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Sala")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Sala {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala") //  ESTA LÍNEA ES LA CLAVE para el imges
    private Integer idSala;


    @Column(name = "numero_sala") //  ESTA LÍNEA ES LA CLAVE para el imges
    private Integer numeroSala;

    @Column(name = "capacidad") //  ESTA LÍNEA ES LA CLAVE para el imges
    private Integer capacidad;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
 
    private List<Asiento> asientos;
    
}
