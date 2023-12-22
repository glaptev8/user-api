package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.WalletType;

import reactor.core.publisher.Mono;

public interface WalletTypeRepository extends R2dbcRepository<WalletType, Long> {
  Mono<Boolean> existsById(Long id);
}
