package com.cinemaroyale.payment;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Se activa cuando payment.gateway=bank
@Component
@ConditionalOnProperty(name = "payment.gateway", havingValue = "bank")
public class BankPaymentGateway implements PaymentGateway {

    // TODO: Inyectar RestTemplate o WebClient para llamar la API del banco
    // @Value("${bank.api.url}") private String bankApiUrl;

    @Override
    public PaymentResult processPayment(BigDecimal amount, Integer idMetodoPago, Integer idUsuario) {
        // Aqui se conectara con la API del sistema bancario del otro equipo.
        // Ejemplo de llamada esperada:
        //
        // BankRequest req = new BankRequest(amount, idMetodoPago, idUsuario);
        // BankResponse res = restTemplate.postForObject(bankApiUrl + "/transactions", req, BankResponse.class);
        // return new PaymentResult(res.isApproved(), res.getTransactionId(), res.getMessage());

        throw new UnsupportedOperationException(
            "Pasarela bancaria no configurada. Contacte al equipo de desarrollo del banco."
        );
    }
}
