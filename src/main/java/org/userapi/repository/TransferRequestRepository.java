package org.userapi.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.TransferRequest;

import reactor.core.publisher.Mono;

public interface TransferRequestRepository extends R2dbcRepository<TransferRequest, Long>  {
  Mono<TransferRequest> findByUid(UUID uuid);
}
