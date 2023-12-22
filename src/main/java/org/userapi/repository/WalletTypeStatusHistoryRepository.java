package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.WalletTypeStatusHistory;

public interface WalletTypeStatusHistoryRepository extends R2dbcRepository<WalletTypeStatusHistory, Long>  {
}
