package org.userapi.service.api;

import org.userapi.dto.WalletTypeSaveDto;
import org.userapi.entity.WalletType;

import reactor.core.publisher.Mono;

public interface WalletTypeService {
  Mono<WalletTypeSaveDto> save(WalletTypeSaveDto walletType);
}
