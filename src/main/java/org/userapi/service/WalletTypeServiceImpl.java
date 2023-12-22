package org.userapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.userapi.dto.WalletTypeSaveDto;
import org.userapi.repository.WalletTypeRepository;
import org.userapi.repository.WalletTypeStatusHistoryRepository;
import org.userapi.service.api.WalletTypeService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletTypeServiceImpl implements WalletTypeService {

  private final WalletTypeRepository walletTypeRepository;
  private final WalletTypeStatusHistoryRepository walletTypeStatusHistoryRepository;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<WalletTypeSaveDto> save(WalletTypeSaveDto walletTypeSaveDto) {
    return transactionalOperator.transactional(
      Mono.defer(() -> {
          if (walletTypeSaveDto.getWalletType().getId() != null) {
            return walletTypeRepository.findById(walletTypeSaveDto.getWalletType().getId())
              .flatMap(oldWallet -> {
                walletTypeSaveDto.getWalletTypeStatusHistory().setFromStatus(oldWallet.getStatus());
                return Mono.just(walletTypeSaveDto);
              });
          }
          return Mono.just(walletTypeSaveDto);
        })
        .then(Mono.defer(() -> walletTypeRepository.save(walletTypeSaveDto.getWalletType())
          .flatMap(walletType -> walletTypeStatusHistoryRepository.save(walletTypeSaveDto.getWalletTypeStatusHistory())
            .flatMap(walletTypeStatusHistory -> {
              walletTypeSaveDto.setWalletType(walletType);
              walletTypeSaveDto.setWalletTypeStatusHistory(walletTypeStatusHistory);
              return Mono.just(walletTypeSaveDto);
            })))));
  }
}
