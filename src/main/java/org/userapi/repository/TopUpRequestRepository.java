package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.TopUpRequest;

public interface TopUpRequestRepository extends R2dbcRepository<TopUpRequest, Long> {
}
