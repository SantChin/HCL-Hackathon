package com.hcl.payment_processor.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentOutcome(UUID paymentId,

		String debitAccountId,

		String creditAccountId,

		BigDecimal amount,

		String currency,

		String status,

		Instant processedAt,

		Long processingTimeMs) {

}
