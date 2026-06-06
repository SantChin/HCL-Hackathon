package com.hcl.payment_ingestor.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hcl.payment_ingestor.dto.PaymentEvent;

@Service
public class PaymentProducer {

	private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
	private final String topic;

	public PaymentProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate,
			@Value("${app.kafka.submitted-topic}") String topic) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = topic;
	}

	public void publish(PaymentEvent event) {

		kafkaTemplate.send(topic, event.debitAccountId(), event);
	}
}
