package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.Transaction;

public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
}
