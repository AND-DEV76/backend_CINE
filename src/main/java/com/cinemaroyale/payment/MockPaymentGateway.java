package com.cinemaroyale.payment;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

// Se activa cuando payment.gateway=mock (valor por defecto)
@Component
@ConditionalOnProperty(name = "payment.gateway", havingValue = "mock", matchIfMissing = true)
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentResult processPayment(BigDecimal amount, Integer idMetodoPago, Integer idUsuario) {
        // Simula un procesamiento exitoso con referencia generada
        String ref = "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, ref, "Pago aprobado (modo demo)");
    }
}
