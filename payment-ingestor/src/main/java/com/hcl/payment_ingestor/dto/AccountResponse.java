package com.hcl.payment_ingestor.dto;

import java.time.LocalDate;

public record AccountResponse (
	String accountId,
	String accountName,
	String accountType,
	String status,
	String currency,
	LocalDate openedDate) {}

