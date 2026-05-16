package com.cinemaroyale.service;

import com.cinemaroyale.dto.BoletoDTO;
import com.cinemaroyale.dto.CompraDTO;
import com.cinemaroyale.model.*;
import com.cinemaroyale.payment.PaymentGateway;
import com.cinemaroyale.payment.PaymentResult;
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
    @Autowired
    private PaymentGateway paymentGateway;

    @Override
    @Transactional
    public List<BoletoDTO> procesarCompra(CompraDTO dto) {
        // 1. Obtener usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Obtener o crear cliente
        Cliente cliente = clienteRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseGet(() -> {
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setUsuario(usuario);
                    return clienteRepository.save(nuevoCliente);
                });

        // 3. Obtener metodo de pago y funcion
        MetodoPago metodoPago = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("Metodo de pago no encontrado"));
        Funcion funcion = funcionRepository.findById(dto.getIdFuncion())
                .orElseThrow(() -> new RuntimeException("Funcion no encontrada"));

        // 4. Verificar disponibilidad con bloqueo pesimista
        for (Integer idAsiento : dto.getIdAsientos()) {
            EstadoAsiento estado = estadoAsientoRepository
                    .findForUpdate(funcion.getIdFuncion(), idAsiento)
                    .orElseThrow(() -> new RuntimeException("Asiento no valido para esta funcion"));

            // Aceptar si esta DISPONIBLE o RESERVADO por este usuario
            boolean disponible = "DISPONIBLE".equals(estado.getEstado());
            boolean reservadoPorEsteUsuario = "RESERVADO".equals(estado.getEstado())
                    && dto.getIdUsuario().equals(estado.getReservadoPor());

            if (!disponible && !reservadoPorEsteUsuario) {
                throw new RuntimeException("El asiento " + estado.getAsiento().getFila()
                        + estado.getAsiento().getNumero() + " ya esta ocupado");
            }
        }

        // 5. Procesar pago a traves de la pasarela
        PaymentResult paymentResult = paymentGateway.processPayment(
                dto.getTotal(), dto.getIdMetodoPago(), dto.getIdUsuario());

        // 6. Crear compra con estado segun resultado del pago
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setMetodoPago(metodoPago);
        compra.setTotal(dto.getTotal());
        compra.setTransaccionRef(paymentResult.getTransactionRef());
        compra.setGatewayResponse(paymentResult.getMessage());

        if (!paymentResult.isApproved()) {
            compra.setEstado("RECHAZADA");
            compraRepository.save(compra);

            // Liberar asientos reservados por este usuario
            List<EstadoAsiento> reservados = estadoAsientoRepository
                    .findByFuncionAndReservadoPor(funcion.getIdFuncion(), dto.getIdUsuario());
            for (EstadoAsiento e : reservados) {
                e.setEstado("DISPONIBLE");
                e.setReservadoHasta(null);
                e.setReservadoPor(null);
                estadoAsientoRepository.save(e);
            }

            throw new RuntimeException("Pago rechazado: " + paymentResult.getMessage());
        }

        compra.setEstado("COMPLETADA");
        Compra compraGuardada = compraRepository.save(compra);

        // 7. Crear boletos y marcar asientos como OCUPADO en batch
        BigDecimal precioPorBoleto = dto.getTotal().divide(new BigDecimal(dto.getIdAsientos().size()));
        List<EstadoAsiento> estadosAOcupar = new ArrayList<>();
        List<Boleto> boletosACrear = new ArrayList<>();

        for (Integer idAsiento : dto.getIdAsientos()) {
            EstadoAsiento estado = estadoAsientoRepository
                    .findByFuncionIdFuncionAndAsientoIdAsiento(funcion.getIdFuncion(), idAsiento).get();
            estado.setEstado("OCUPADO");
            estado.setReservadoHasta(null);
            estado.setReservadoPor(null);
            estadosAOcupar.add(estado);

            Boleto boleto = new Boleto();
            boleto.setFuncion(funcion);
            boleto.setAsiento(estado.getAsiento());
            boleto.setCompra(compraGuardada);
            boleto.setPrecio(precioPorBoleto);
            boleto.setEstado("ACTIVO");
            boletosACrear.add(boleto);
        }

        estadoAsientoRepository.saveAll(estadosAOcupar);
        List<Boleto> boletosCreados = boletoRepository.saveAll(boletosACrear);

        return boletosCreados.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BoletoDTO> obtenerBoletosPorUsuario(Integer idUsuario) {
        return boletoRepository.findAllByUsuarioId(idUsuario)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
