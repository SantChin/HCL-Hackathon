package com.hcl.payment_ingestor.dto;

import java.time.LocalDateTime;

public record ErrorResponse(

		LocalDateTime timestamp,

		int status,

		String error,

		String message

) {
}
