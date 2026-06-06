# HCL-Hackathon
# PayStream - Event Driven Payment Processing System

## Overview

PayStream is a Spring Boot microservices application that demonstrates event-driven payment processing using Apache Kafka.

The system consists of two services:

1. **payment-ingestor**
   * Accepts payment requests
   * Validates accounts and requests
   * Prevents duplicate payments
   * Publishes payment events to Kafka
2. **payment-processor**
   * Consumes payment events from Kafka
   * Applies business processing rules
   * Stores payment outcomes
   * Publishes processed payment outcomes to another Kafka topic
---
## Architecture

Client
|
v
payment-ingestor
|
v
payments.submitted (Kafka Topic)
|
v
payment-processor
|
+--> MySQL (payment outcomes)
|
v
payments.processed (Kafka Topic)
---
## Technology Stack

* Java 21
* Spring Boot 3.3.1
* Spring Data JPA
* Apache Kafka
* MySQL
* Maven

---

## Kafka Topics

### payments.submitted

Published by:

* payment-ingestor

Consumed by:

* payment-processor

Purpose:

* Represents a newly submitted payment request.

### payments.processed

Published by:

* payment-processor

Purpose:

* Represents the final payment processing outcome.

---

## Running the Application

### Start Infrastructure

Start Kafka and Zookeeper.

Verify topics:

```bash
kafka-topics --list --bootstrap-server localhost:9092
```

Expected:

```text
payments.submitted
payments.processed
```

---

### Start payment-ingestor

Runs on:

```text
http://localhost:8080
```

---

### Start payment-processor

Runs on:

```text
http://localhost:8081
```

---

## API

### Submit Payment

```http
POST /api/payments
```

Sample Request:

```json
{
  "paymentId":"910e8400-e29b-41d4-a716-446655440000",
  "debitAccountId":"09-01-28/11223344",
  "creditAccountId":"09-01-28/55667788",
  "amount":1000,
  "currency":"GBP",
  "reference":"Salary",
  "timestamp":"2026-06-05T10:30:00Z"
}
```

Response:

```http
202 Accepted
```

```json
{
  "paymentId":"550e8400-e29b-41d4-a716-446655440000"
}
```

---

### Get Account Details

```http
GET /api/accounts/{accountId}
```

---

## Validation & Error Handling

| Status | Description       |
| ------ | ----------------- |
| 400    | Validation Error  |
| 404    | Account Not Found |
| 409    | Duplicate Payment |
| 422    | Account Suspended |

---

## Processing Rules

Current implementation:

* Amount <= 250000 → PROCESSED
* Amount > 250000 → HELD

The processor stores the outcome and publishes a PaymentOutcome event to Kafka.

---

## End-to-End Flow

1. Client submits payment.
2. Ingestor validates request and account information.
3. Ingestor publishes PaymentEvent to `payments.submitted`.
4. Processor consumes PaymentEvent.
5. Processor applies business rules.
6. Processor stores outcome in database.
7. Processor publishes PaymentOutcome to `payments.processed`.

---

## Future Enhancements

* Reporting APIs
* Payment history APIs
* Notification Service
* Audit Service
* Metrics Dashboard
* Dead Letter Queue (DLQ)
* Retry Mechanism
