package com.hcl.payment_ingestor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.payment_ingestor.dto.PaymentRequest;
import com.hcl.payment_ingestor.dto.PaymentResponse;
import com.hcl.payment_ingestor.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<PaymentResponse> submitPayment(@Valid @RequestBody PaymentRequest request) {

		return ResponseEntity.accepted().body(new PaymentResponse(paymentService.submitPayment(request)));
	}
}
