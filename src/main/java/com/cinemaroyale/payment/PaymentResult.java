package com.cinemaroyale.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResult {
    private boolean approved;
    private String transactionRef;
    private String message;
}
