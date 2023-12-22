package org.userapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.userapi.dto.TopUpSaveDto;
import org.userapi.entity.PaymentRequest;
import org.userapi.entity.TopUpRequest;
import org.userapi.entity.Transaction;
import org.userapi.repository.PaymentRequestRepository;
import org.userapi.repository.TopUpRequestRepository;
import org.userapi.repository.TransactionRepository;
import org.userapi.repository.WalletRepository;
import org.userapi.repository.WalletTypeRepository;
import org.userapi.service.api.TopUpRequestService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TopUpRequestServiceImpl implements TopUpRequestService {

  private final TransactionRepository transactionRepository;
  private final TopUpRequestRepository topUpRequestRepository;
  private final PaymentRequestRepository paymentRequestRepository;
  private final TransactionalOperator transactionalOperator;
  private final WalletRepository walletRepository;
  private final WalletTypeRepository walletTypeRepository;

  @Override
  public Mono<TopUpSaveDto> save(TopUpSaveDto topUpSaveDto) {
    return transactionalOperator.transactional(
      savePaymentRequest(topUpSaveDto.getPaymentRequest())
        .flatMap(payment -> {
          topUpSaveDto.getTransaction().setPaymentRequestUid(payment.getUid());
          topUpSaveDto.getTopUpRequest().setPaymentRequestUid(payment.getUid());
          return setWalletToTransaction(topUpSaveDto)
            .flatMap(transaction -> saveTransaction(transaction)
              .flatMap(savedTransaction -> saveTopUpRequest(topUpSaveDto.getTopUpRequest())
                .flatMap(topUp -> {
                  topUpSaveDto.setTopUpRequest(topUp);
                  topUpSaveDto.setPaymentRequest(payment);
                  topUpSaveDto.setTransaction(transaction);
                  return Mono.just(topUpSaveDto);
                })));
        })
    );
  }

  private Mono<Transaction> setWalletToTransaction(TopUpSaveDto topUpSaveDto) {
    return walletRepository.findByWalletUid(topUpSaveDto.getPaymentRequest().getWalletUid())
      .flatMap(wallet -> {
        topUpSaveDto.getTransaction().setWalletName(wallet.getName());
        return walletTypeRepository.findById(wallet.getWalletTypeId())
          .flatMap(walletType -> {
            topUpSaveDto.getTransaction().setCurrencyCode(walletType.getCurrencyCode());
            return Mono.just(topUpSaveDto.getTransaction());
          });
      })
      .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  private Mono<TopUpRequest> saveTopUpRequest(TopUpRequest topUpRequest) {
    return topUpRequestRepository.save(topUpRequest);
  }

  private Mono<PaymentRequest> savePaymentRequest(PaymentRequest paymentRequest) {
    return paymentRequestRepository.save(paymentRequest);
  }

  private Mono<Transaction> saveTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }
}
