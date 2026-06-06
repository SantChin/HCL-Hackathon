package com.hcl.payment_processor.dto;

public class MetricsSummaryResponse {

	private long totalProcessed;

	private long totalHeld;

	private long totalRejected;

	private double avgProcessingTimeMs;
}