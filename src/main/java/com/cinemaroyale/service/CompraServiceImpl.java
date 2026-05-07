package com.cinemaroyale.service;

import com.cinemaroyale.dto.BoletoDTO;
import com.cinemaroyale.dto.CompraDTO;
import com.cinemaroyale.model.*;
import com.cinemaroyale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private BoletoRepository boletoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    @Autowired
    private FuncionRepository funcionRepository;
    @Autowired
    private AsientoRepository asientoRepository;
    @Autowired
    private EstadoAsientoRepository estadoAsientoRepository;

    @Override
    @Transactional
    public List<BoletoDTO> procesarCompra(CompraDTO dto) {
        // 1. Obtener Usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Obtener o crear Cliente
        Cliente cliente = clienteRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseGet(() -> {
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setUsuario(usuario);
                    return clienteRepository.save(nuevoCliente);
                });

        // 3. Obtener Método de Pago y Función
        MetodoPago metodoPago = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        Funcion funcion = funcionRepository.findById(dto.getIdFuncion())
                .orElseThrow(() -> new RuntimeException("Función no encontrada"));

        // 4. Verificar disponibilidad de asientos
        for (Integer idAsiento : dto.getIdAsientos()) {
            EstadoAsiento estado = estadoAsientoRepository.findByFuncionIdFuncionAndAsientoIdAsiento(funcion.getIdFuncion(), idAsiento)
                    .orElseThrow(() -> new RuntimeException("Asiento no válido para esta función"));
            if (!"DISPONIBLE".equals(estado.getEstado())) {
                throw new RuntimeException("El asiento " + estado.getAsiento().getFila() + estado.getAsiento().getNumero() + " ya está ocupado");
            }
        }

        // 5. Crear Compra
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setMetodoPago(metodoPago);
        compra.setTotal(dto.getTotal());
        compra.setEstado("COMPLETADA");
        Compra compraGuardada = compraRepository.save(compra);

        // 6. Crear Boletos y actualizar estado
        List<Boleto> boletosCreados = new ArrayList<>();
        BigDecimal precioPorBoleto = dto.getTotal().divide(new BigDecimal(dto.getIdAsientos().size()));

        for (Integer idAsiento : dto.getIdAsientos()) {
            // Actualizar estado
            EstadoAsiento estado = estadoAsientoRepository.findByFuncionIdFuncionAndAsientoIdAsiento(funcion.getIdFuncion(), idAsiento).get();
            estado.setEstado("OCUPADO");
            estadoAsientoRepository.save(estado);

            // Crear boleto
            Boleto boleto = new Boleto();
            boleto.setFuncion(funcion);
            boleto.setAsiento(estado.getAsiento());
            boleto.setCompra(compraGuardada);
            boleto.setPrecio(precioPorBoleto);
            boleto.setEstado("ACTIVO");
            boletosCreados.add(boletoRepository.save(boleto));
        }

        // 7. Retornar DTOs
        return boletosCreados.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BoletoDTO> obtenerBoletosPorUsuario(Integer idUsuario) {
        Cliente cliente = clienteRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        List<Compra> compras = compraRepository.findByClienteIdCliente(cliente.getIdCliente());
        List<Boleto> boletos = new ArrayList<>();
        for (Compra compra : compras) {
            boletos.addAll(boletoRepository.findByCompraIdCompra(compra.getIdCompra()));
        }

        return boletos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private BoletoDTO mapToDTO(Boleto b) {
        BoletoDTO dto = new BoletoDTO();
        dto.setIdBoleto(b.getIdBoleto());
        dto.setNombrePelicula(b.getFuncion().getPelicula().getNombre());
        dto.setNumeroSala(b.getFuncion().getSala().getNumeroSala());
        dto.setFecha(b.getFuncion().getFechaHora().toLocalDate());
        dto.setHora(b.getFuncion().getFechaHora().toLocalTime());
        dto.setFila(b.getAsiento().getFila());
        dto.setNumeroAsiento(b.getAsiento().getNumero());
        dto.setPrecio(b.getPrecio());
        dto.setEstado(b.getEstado());
        return dto;
    }
}
