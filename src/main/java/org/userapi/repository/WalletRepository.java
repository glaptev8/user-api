package org.userapi.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.Wallet;

import reactor.core.publisher.Mono;

public interface WalletRepository extends R2dbcRepository<Wallet, Long> {
  Mono<Wallet> findByWalletUid(UUID uuid);
}
