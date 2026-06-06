package com.hcl.payment_ingestor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.payment_ingestor.entity.ProcessedPaymentEntity;

@Repository
public interface ProcessedPaymentRepository extends JpaRepository<ProcessedPaymentEntity, UUID> {

}
