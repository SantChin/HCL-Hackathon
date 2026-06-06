package com.hcl.payment_processor.service;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.hcl.payment_processor.dto.PaymentEvent;
import com.hcl.payment_processor.dto.PaymentOutcome;
import com.hcl.payment_processor.entity.PaymentOutcomeEntity;
import com.hcl.payment_processor.kafka.PaymentOutcomeProducer;
import com.hcl.payment_processor.repository.PaymentOutcomeRepository;

@Service
public class PaymentProcessingService {

	private final PaymentOutcomeRepository repository;
    private final PaymentOutcomeProducer paymentOutcomeProducer;

    public PaymentProcessingService(
            PaymentOutcomeRepository repository,
            PaymentOutcomeProducer paymentOutcomeProducer) {

        this.repository = repository;
        this.paymentOutcomeProducer = paymentOutcomeProducer;
    }

    public void process(
            PaymentEvent event) {

        long start = System.currentTimeMillis();

        String status =
                event.amount()
                        .compareTo(
                                BigDecimal.valueOf(250000))
                        > 0
                        ? "HELD"
                        : "PROCESSED";

        PaymentOutcomeEntity entity =
                new PaymentOutcomeEntity();

        entity.setPaymentId(event.paymentId());
        entity.setDebitAccountId(
                event.debitAccountId());

        entity.setCreditAccountId(
                event.creditAccountId());

        entity.setAmount(event.amount());

        entity.setCurrency(event.currency());

        entity.setStatus(status);

        entity.setProcessedAt(
                Instant.now());

        entity.setProcessingTimeMs(
                System.currentTimeMillis()
                        - start);

        repository.save(entity);

        PaymentOutcome outcome =
                new PaymentOutcome(
                        event.paymentId(),
                        event.debitAccountId(),
                        event.creditAccountId(),
                        event.amount(),
                        event.currency(),
                        status,
                        entity.getProcessedAt(),
                        entity.getProcessingTimeMs()
                );

        paymentOutcomeProducer.publish(outcome);
    }
}
