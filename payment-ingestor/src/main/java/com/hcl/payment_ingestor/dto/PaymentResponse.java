package com.hcl.payment_ingestor.dto;

import java.util.UUID;

public record PaymentResponse(
        UUID paymentId
) {
}
