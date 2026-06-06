package com.hcl.payment_processor.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.hcl.payment_processor.dto.PaymentEvent;
import com.hcl.payment_processor.service.PaymentProcessingService;

@Service
public class PaymentConsumer {

    private final PaymentProcessingService paymentProcessingService;

    public PaymentConsumer(
            PaymentProcessingService paymentProcessingService) {

        this.paymentProcessingService =
                paymentProcessingService;
    }

    @KafkaListener(
            topics = "payments.submitted",
            groupId = "payment-processor-group"
    )
    public void consume(
            PaymentEvent paymentEvent) {

        paymentProcessingService
                .process(paymentEvent);
    }
}