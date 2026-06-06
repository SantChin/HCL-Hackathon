package com.hcl.payment_ingestor.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentRequest(
		@NotNull(message = "Payment ID must be a valid UUID") UUID paymentId,

		@NotBlank(message = "Debit account ID must not be blank") String debitAccountId,

		@NotBlank(message = "Credit account ID must not be blank") String creditAccountId,

		@NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than 0") BigDecimal amount,

		@NotBlank @Size(min = 3, max = 3, message = "Currency must be a 3-character ISO code") String currency,

		@Size(max = 35, message = "Reference must not exceed 35 characters") String reference,

		@NotNull @PastOrPresent(message = "Timestamp must not be in the future") Instant timestamp) {
}
