package com.cinemaroyale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// Ejecuta limpieza de reservas expiradas cada 30 segundos
@Component
public class ReservationCleanupJob {

    @Autowired
    private ReservationService reservationService;

    @Scheduled(fixedRate = 30000)
    public void cleanupExpiredReservations() {
        int released = reservationService.liberarReservasExpiradas();
        if (released > 0) {
            System.out.println("[Reservation Cleanup] " + released + " asiento(s) liberado(s)");
        }
    }
}
