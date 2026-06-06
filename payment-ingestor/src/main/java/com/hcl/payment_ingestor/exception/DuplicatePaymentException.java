package com.hcl.payment_ingestor.exception;

public class DuplicatePaymentException extends RuntimeException {

	public DuplicatePaymentException(String message) {
		super(message);
	}
}
