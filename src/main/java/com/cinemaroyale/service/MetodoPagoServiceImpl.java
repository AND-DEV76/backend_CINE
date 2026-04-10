package com.cinemaroyale.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cinemaroyale.dto.MetodoPagoDTO;
import com.cinemaroyale.model.MetodoPago;
import com.cinemaroyale.repository.MetodoPagoRepository;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository repository;

    // Convertir Entity → DTO
    private MetodoPagoDTO toDTO(MetodoPago entity) {
        return new MetodoPagoDTO(
                entity.getIdMetodoPago(),
                entity.getNombre()
        );
    }

    // Convertir DTO → Entity
    private MetodoPago toEntity(MetodoPagoDTO dto) {
        return new MetodoPago(
                dto.getIdMetodoPago(),
                dto.getNombre()
        );
    }

    @Override
    public List<MetodoPagoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MetodoPagoDTO obtenerPorId(Integer id) {
        MetodoPago entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        return toDTO(entity);
    }

    @Override
    public MetodoPagoDTO guardar(MetodoPagoDTO dto) {
        MetodoPago entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public MetodoPagoDTO actualizar(Integer id, MetodoPagoDTO dto) {
        MetodoPago entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        entity.setNombre(dto.getNombre());

        return toDTO(repository.save(entity));
    }

    @Override
    public void eliminar(Integer id) {
        MetodoPago entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        repository.delete(entity);
    }
}