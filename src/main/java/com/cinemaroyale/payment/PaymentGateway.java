package com.cinemaroyale.payment;

import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentResult processPayment(BigDecimal amount, Integer idMetodoPago, Integer idUsuario);
}
