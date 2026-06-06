package com.hcl.payment_processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.payment_processor.entity.PaymentOutcomeEntity;

@Repository
public interface PaymentOutcomeRepository
extends JpaRepository<PaymentOutcomeEntity, Long> {
}
