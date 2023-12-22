package org.userapi.service.api;

import java.util.UUID;

import org.userapi.dto.WalletSaveDto;
import org.userapi.entity.Wallet;

import reactor.core.publisher.Mono;

public interface WalletService {
  Mono<WalletSaveDto> save(WalletSaveDto wallet);

  Mono<Wallet> getByUuid(UUID uuid);
}
