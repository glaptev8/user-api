package org.userapi.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.PaymentRequest;

import reactor.core.publisher.Mono;

public interface PaymentRequestRepository extends R2dbcRepository<PaymentRequest, Long> {
  Mono<PaymentRequest> findByUid(UUID uid);
}
