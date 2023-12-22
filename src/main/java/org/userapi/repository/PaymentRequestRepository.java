package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.PaymentRequest;

public interface PaymentRequestRepository extends R2dbcRepository<PaymentRequest, Long> {
}
