package com.hcl.payment_ingestor.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hcl.payment_ingestor.dto.PaymentEvent;
import com.hcl.payment_ingestor.dto.PaymentRequest;
import com.hcl.payment_ingestor.entity.AccountEntity;
import com.hcl.payment_ingestor.entity.ProcessedPaymentEntity;
import com.hcl.payment_ingestor.exception.AccountNotFoundException;
import com.hcl.payment_ingestor.exception.AccountSuspendedException;
import com.hcl.payment_ingestor.exception.DuplicatePaymentException;
import com.hcl.payment_ingestor.kafka.PaymentProducer;
import com.hcl.payment_ingestor.repository.AccountRepository;
import com.hcl.payment_ingestor.repository.ProcessedPaymentRepository;

@Service
public class PaymentService {

	private final AccountRepository accountRepository;
	private final ProcessedPaymentRepository processedPaymentRepository;
	private final PaymentProducer paymentProducer;

	public PaymentService(AccountRepository accountRepository, ProcessedPaymentRepository processedPaymentRepository,
			PaymentProducer paymentProducer) {
		this.accountRepository = accountRepository;
		this.processedPaymentRepository = processedPaymentRepository;
		this.paymentProducer = paymentProducer;
	}

	public UUID submitPayment(PaymentRequest request) {

		validateDuplicatePayment(request.paymentId());

		AccountEntity debitAccount = getAccountOrThrow(request.debitAccountId(), true);

		AccountEntity creditAccount = getAccountOrThrow(request.creditAccountId(), false);

		validateAccountStatus(debitAccount);
		validateAccountStatus(creditAccount);

		PaymentEvent paymentEvent = new PaymentEvent(request.paymentId(), request.debitAccountId(),
				request.creditAccountId(), request.amount(), request.currency(), request.reference(),
				request.timestamp());

		paymentProducer.publish(paymentEvent);

		ProcessedPaymentEntity processedPayment = new ProcessedPaymentEntity();

		processedPayment.setPaymentId(request.paymentId());

		processedPaymentRepository.save(processedPayment);

		return request.paymentId();
	}

	private void validateDuplicatePayment(UUID paymentId) {

		if (processedPaymentRepository.existsById(paymentId)) {
			throw new DuplicatePaymentException("Duplicate paymentId: " + paymentId);
		}
	}

	private AccountEntity getAccountOrThrow(String accountId, boolean debitAccount) {

		return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(
				debitAccount ? "Debit account not found: " + accountId : "Credit account not found: " + accountId));
	}

	private void validateAccountStatus(AccountEntity account) {

		if ("SUSPENDED".equalsIgnoreCase(account.getStatus())) {

			throw new AccountSuspendedException("Account is suspended: " + account.getAccountId());
		}
	}
}