package org.userapi.service.api;

import org.userapi.entity.CryptoWallet;

import reactor.core.publisher.Mono;

public interface CryptoWalletService {
  Mono<CryptoWallet> save(CryptoWallet cryptoWallet);
}
