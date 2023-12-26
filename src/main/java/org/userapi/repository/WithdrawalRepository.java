package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.WithdrawalRequest;

public interface WithdrawalRepository extends R2dbcRepository<WithdrawalRequest, Long>  {
}
