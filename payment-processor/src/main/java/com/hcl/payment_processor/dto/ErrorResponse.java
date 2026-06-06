package com.hcl.payment_processor.dto;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {

	private Instant timestamp;

	private int status;

	private String error;

	private String path;

	private List<Violation> violations;
}