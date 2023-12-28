package org.userapi.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.leantech.common.dto.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.userapi.dto.WithdrawalSaveDto;
import org.userapi.entity.Transaction;
import org.userapi.repository.PaymentRequestRepository;
import org.userapi.repository.TransactionRepository;
import org.userapi.repository.WalletRepository;
import org.userapi.repository.WalletTypeRepository;
import org.userapi.repository.WithdrawalRepository;
import org.userapi.service.api.WithdrawalService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.leantech.common.dto.TransactionState.WITHDRAWAL;
import static org.leantech.common.dto.TransactionType.CREATED;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {

  private final WithdrawalRepository withdrawalRepository;
  private final TransactionRepository transactionRepository;
  private final PaymentRequestRepository paymentRequestRepository;
  private final TransactionalOperator transactionalOperator;
  private final WalletRepository walletRepository;
  private final WalletTypeRepository walletTypeRepository;

  @Override
  public Mono<WithdrawalSaveDto> save(WithdrawalSaveDto withdrawalDto) {
    return transactionalOperator.transactional(
      paymentRequestRepository.save(withdrawalDto.getPaymentRequest())
        .flatMap(payment -> {
          withdrawalDto.getTransaction().setPaymentRequestUid(payment.getUid());
          withdrawalDto.getWithdrawalRequest().setPaymentRequestUid(payment.getUid());
          return setWalletToTransaction(withdrawalDto)
            .flatMap(transaction -> transactionRepository.save(transaction)
              .flatMap(savedTransaction -> withdrawalRepository.save(withdrawalDto.getWithdrawalRequest())
                .flatMap(withDrawal -> {
                  withdrawalDto.setWithdrawalRequest(withDrawal);
                  withdrawalDto.setPaymentRequest(payment);
                  withdrawalDto.setTransaction(transaction);
                  return Mono.just(withdrawalDto);
                })));
        })
    );
  }

  @Override
  public Mono<Boolean> confirm(UUID paymentRequestUid) {
    return transactionalOperator.transactional(
      transactionRepository.findByPaymentRequestUid(paymentRequestUid)
        .flatMap(transaction -> {
          if (transaction.getState() == WITHDRAWAL && transaction.getType() == CREATED) {
            return walletRepository.findByWalletUid(transaction.getWalletUid())
              .flatMap(wallet -> walletTypeRepository.findById(wallet.getWalletTypeId())
                .flatMap(walletType -> {
                  if (walletType.getCurrencyCode() == transaction.getCurrencyCode()) {
                    return paymentRequestRepository.findByUid(transaction.getPaymentRequestUid())
                      .flatMap(paymentRequest -> {
                        BigDecimal rawAmount;
                        if (paymentRequest != null && paymentRequest.getCryptoWalletUid() != null && paymentRequest.getAmountInCrypto() != null) {
                          rawAmount =  paymentRequest.getAmountInCrypto();
                        }
                        else {
                          rawAmount = new BigDecimal(transaction.getRawAmount());
                        }
                        if (wallet.getBalance().compareTo(rawAmount) > 0) {
                          wallet.setBalance(wallet.getBalance().subtract(rawAmount));
                          return walletRepository.save(wallet)
                            .then(Mono.defer(() -> {
                              transaction.setType(TransactionType.COMPLETED);
                              return transactionRepository.save(transaction);
                            }))
                            .then(Mono.just(Boolean.TRUE));
                        }
                        else {
                          return Mono.error(new RuntimeException());
                        }
                      });
                  }
                  return Mono.error(new RuntimeException());
                })
                .switchIfEmpty(Mono.error(new RuntimeException())));
          }
          return Mono.error(new RuntimeException());
        })
    );
  }

  private Mono<Transaction> setWalletToTransaction(WithdrawalSaveDto withdrawalDto) {
    return walletRepository.findByWalletUid(withdrawalDto.getPaymentRequest().getWalletUid())
      .flatMap(wallet -> {
        withdrawalDto.getTransaction().setWalletName(wallet.getName());
        return walletTypeRepository.findById(wallet.getWalletTypeId())
          .flatMap(walletType -> {
            withdrawalDto.getTransaction().setCurrencyCode(walletType.getCurrencyCode());
            return Mono.just(withdrawalDto.getTransaction());
          });
      })
      .switchIfEmpty(Mono.error(new RuntimeException()));
  }
}
