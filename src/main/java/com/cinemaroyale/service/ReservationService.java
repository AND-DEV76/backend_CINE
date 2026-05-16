package com.cinemaroyale.service;

import com.cinemaroyale.model.EstadoAsiento;
import com.cinemaroyale.repository.EstadoAsientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private static final int RESERVATION_MINUTES = 5;

    @Autowired
    private EstadoAsientoRepository estadoAsientoRepository;

    // Reserva asientos temporalmente por 5 minutos
    @Transactional
    public void reservarAsientos(Integer idFuncion, List<Integer> idAsientos, Integer idUsuario) {
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(RESERVATION_MINUTES);

        for (Integer idAsiento : idAsientos) {
            EstadoAsiento estado = estadoAsientoRepository
                    .findForUpdate(idFuncion, idAsiento)
                    .orElseThrow(() -> new RuntimeException("Asiento no valido para esta funcion"));

            if (!"DISPONIBLE".equals(estado.getEstado())) {
                throw new RuntimeException("Asiento " + estado.getAsiento().getFila()
                        + estado.getAsiento().getNumero() + " no esta disponible");
            }

            estado.setEstado("RESERVADO");
            estado.setReservadoHasta(expiry);
            estado.setReservadoPor(idUsuario);
            estadoAsientoRepository.save(estado);
        }
    }

    // Libera asientos reservados por un usuario en una funcion
    @Transactional
    public void liberarAsientos(Integer idFuncion, Integer idUsuario) {
        List<EstadoAsiento> reservados = estadoAsientoRepository
                .findByFuncionAndReservadoPor(idFuncion, idUsuario);

        for (EstadoAsiento estado : reservados) {
            estado.setEstado("DISPONIBLE");
            estado.setReservadoHasta(null);
            estado.setReservadoPor(null);
            estadoAsientoRepository.save(estado);
        }
    }

    // Libera todas las reservas expiradas (llamado por el scheduled job)
    @Transactional
    public int liberarReservasExpiradas() {
        List<EstadoAsiento> expirados = estadoAsientoRepository
                .findExpiredReservations(LocalDateTime.now());

        for (EstadoAsiento estado : expirados) {
            estado.setEstado("DISPONIBLE");
            estado.setReservadoHasta(null);
            estado.setReservadoPor(null);
            estadoAsientoRepository.save(estado);
        }

        return expirados.size();
    }
}
