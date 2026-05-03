package com.cinemaroyale.controller;

import com.cinemaroyale.dto.BoletoDTO;
import com.cinemaroyale.dto.CompraDTO;
import com.cinemaroyale.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<?> procesarCompra(@RequestBody CompraDTO dto) {
        try {
            List<BoletoDTO> boletos = compraService.procesarCompra(dto);
            return ResponseEntity.ok(boletos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<BoletoDTO>> obtenerBoletosUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(compraService.obtenerBoletosPorUsuario(idUsuario));
    }
}
