package com.cinemaroyale.controller;

import com.cinemaroyale.service.ReservationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> reservar(@RequestBody ReservaRequest request) {
        try {
            reservationService.reservarAsientos(
                    request.getIdFuncion(),
                    request.getIdAsientos(),
                    request.getIdUsuario()
            );
            return ResponseEntity.ok("Asientos reservados por 5 minutos");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/liberar")
    public ResponseEntity<?> liberar(@RequestBody LiberarRequest request) {
        reservationService.liberarAsientos(request.getIdFuncion(), request.getIdUsuario());
        return ResponseEntity.ok("Asientos liberados");
    }

    @Data
    public static class ReservaRequest {
        private Integer idFuncion;
        private List<Integer> idAsientos;
        private Integer idUsuario;
    }

    @Data
    public static class LiberarRequest {
        private Integer idFuncion;
        private Integer idUsuario;
    }
}
