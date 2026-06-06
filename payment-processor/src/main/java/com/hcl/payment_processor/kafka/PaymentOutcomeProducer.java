
package com.hcl.payment_processor.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hcl.payment_processor.dto.PaymentOutcome;

@Service
public class PaymentOutcomeProducer {

	private final KafkaTemplate<String, PaymentOutcome> kafkaTemplate;
	private String processedTopic;

	public PaymentOutcomeProducer(KafkaTemplate<String, PaymentOutcome> kafkaTemplate,
			@Value("${app.kafka.processed-topic}") String processedTopic
			) {
		this.kafkaTemplate = kafkaTemplate;
		this.processedTopic = processedTopic;
	}

	public void publish(PaymentOutcome outcome) {

		kafkaTemplate.send(processedTopic, outcome.paymentId().toString(), outcome);

	}
}