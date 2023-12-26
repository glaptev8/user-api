package org.userapi.service;

import org.springframework.stereotype.Service;
import org.userapi.entity.CryptoWallet;
import org.userapi.repository.CryptoWalletRepository;
import org.userapi.repository.WalletRepository;
import org.userapi.service.api.CryptoWalletService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CryptoWalletServiceImpl implements CryptoWalletService {

  private final CryptoWalletRepository cryptoWalletRepository;
  private final WalletRepository walletRepository;

  @Override
  public Mono<CryptoWallet> save(CryptoWallet cryptoWallet) {
    return walletRepository.existsByWalletUid(cryptoWallet.getWalletUid())
      .flatMap(exist -> {
        if (exist) {
          return cryptoWalletRepository.save(cryptoWallet);
        }
        return Mono.error(new RuntimeException(""));
      });
  }
}
