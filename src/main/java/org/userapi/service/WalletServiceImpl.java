package org.userapi.service;

import java.util.UUID;

import org.leantech.common.dto.WalletDto;
import org.leantech.common.dto.WalletTypeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.userapi.dto.WalletSaveDto;
import org.userapi.entity.Wallet;
import org.userapi.entity.WalletType;
import org.userapi.mapper.MapStructMapper;
import org.userapi.repository.WalletRepository;
import org.userapi.repository.WalletStatusHistoryRepository;
import org.userapi.repository.WalletTypeRepository;
import org.userapi.service.api.WalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final WalletStatusHistoryRepository walletStatusHistoryRepository;
  private final MapStructMapper mapper;
  private final WalletTypeRepository walletTypeRepository;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<WalletSaveDto> save(WalletSaveDto walletSaveDto) {
    return transactionalOperator.transactional(
        walletTypeRepository.existsById(walletSaveDto.getWallet().getId()))
      .flatMap(exist -> {
        if (exist) {
          if (walletSaveDto.getWallet().getWalletUid() != null) {
            return walletRepository.findByWalletUid(walletSaveDto.getWallet().getWalletUid())
              .flatMap(oldWallet -> {
                walletSaveDto.getWalletStatusHistory().setFromStatus(oldWallet.getStatus());
                return Mono.just(walletSaveDto);
              });
          }
          return Mono.just(walletSaveDto);
        }
        return Mono.error(new RuntimeException());
      })
      .then(Mono.defer(() -> walletRepository.save(walletSaveDto.getWallet())
        .flatMap(wallet -> {
          walletSaveDto.getWalletStatusHistory().setWalletUid(wallet.getWalletUid());
          return walletStatusHistoryRepository.save(walletSaveDto.getWalletStatusHistory())
            .flatMap(walletStatusHistory -> {
              walletSaveDto.setWalletStatusHistory(walletStatusHistory);
              walletSaveDto.setWallet(wallet);
              return Mono.just(walletSaveDto);
            });
        })));
  }

  @Override
  public Mono<WalletDto> getByUuid(UUID uuid) {
    return walletRepository.findByWalletUid(uuid)
      .flatMap(wallet -> walletTypeRepository.findById(wallet.getWalletTypeId())
        .map(walletType -> {
          log.info("wallet was found {}", wallet);
          var walletDto = mapper.walletDto(wallet);
          walletDto.setWalletType(WalletTypeDto.builder()
                                      .currencyCode(walletType.getCurrencyCode())
                                      .build());
          log.info("wallet was mapped {}", walletDto);
          return walletDto;
        }));
  }
}
