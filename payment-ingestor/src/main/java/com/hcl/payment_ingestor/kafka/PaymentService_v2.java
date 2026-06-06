package com.hcl.payment_ingestor.kafka;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hcl.payment_ingestor.dto.PaymentEvent;
import com.hcl.payment_ingestor.dto.PaymentRequest;
import com.hcl.payment_ingestor.entity.AccountEntity;
import com.hcl.payment_ingestor.entity.ProcessedPaymentEntity;
import com.hcl.payment_ingestor.exception.AccountNotFoundException;
import com.hcl.payment_ingestor.exception.AccountSuspendedException;
import com.hcl.payment_ingestor.exception.DuplicatePaymentException;
import com.hcl.payment_ingestor.repository.AccountRepository;
import com.hcl.payment_ingestor.repository.ProcessedPaymentRepository;

@Service
public class PaymentService_v2 {

    private final AccountRepository accountRepository;
    private final ProcessedPaymentRepository processedPaymentRepository;
    private final PaymentProducer paymentProducer;

    public PaymentService_v2(AccountRepository accountRepository,
                          ProcessedPaymentRepository processedPaymentRepository,
                          PaymentProducer paymentProducer) {

        this.accountRepository = accountRepository;
        this.processedPaymentRepository = processedPaymentRepository;
        this.paymentProducer = paymentProducer;
    }

    public UUID submitPayment(PaymentRequest request) {

        if(processedPaymentRepository.existsById(request.paymentId())) {
            throw new DuplicatePaymentException(
                    "Duplicate paymentId"
            );
        }

        AccountEntity debitAccount =
                accountRepository.findById(
                        request.debitAccountId())
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        "Debit account not found: "
                                                + request.debitAccountId()));

        AccountEntity creditAccount =
                accountRepository.findById(
                        request.creditAccountId())
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        "Credit account not found: "
                                                + request.creditAccountId()));

        validateAccountStatus(debitAccount);
        validateAccountStatus(creditAccount);

        PaymentEvent event = new PaymentEvent(
        	    request.paymentId(),
        	    request.debitAccountId(),
        	    request.creditAccountId(),
        	    request.amount(),
        	    request.currency(),
        	    request.reference(),
        	    request.timestamp()
        	);


        paymentProducer.publish(event);

        ProcessedPaymentEntity processed =
                new ProcessedPaymentEntity();

        processed.setPaymentId(request.paymentId());

        processedPaymentRepository.save(processed);

        return request.paymentId();
    }

    private void validateAccountStatus(AccountEntity account) {

        if("SUSPENDED".equalsIgnoreCase(account.getStatus())) {

            throw new AccountSuspendedException(
                    "Account is suspended: "
                            + account.getAccountId()
            );
        }
    }
}
