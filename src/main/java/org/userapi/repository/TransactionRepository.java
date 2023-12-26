package org.userapi.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.Transaction;

import reactor.core.publisher.Mono;

public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
  Mono<Transaction> findByUid(UUID uid);

  Mono<Transaction> findByPaymentRequestUid(UUID uid);
}
