package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.WalletStatusHistory;

public interface WalletStatusHistoryRepository extends R2dbcRepository<WalletStatusHistory, Long> {
}
