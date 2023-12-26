package org.userapi.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.userapi.dto.TransferRequestSaveDto;
import org.userapi.repository.PaymentRequestRepository;
import org.userapi.repository.TransactionRepository;
import org.userapi.repository.TransferRequestRepository;
import org.userapi.repository.WalletRepository;
import org.userapi.repository.WalletTypeRepository;
import org.userapi.service.api.TransferRequestService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.userapi.dto.TransactionState.TRANSFER_IN;
import static org.userapi.dto.TransactionState.TRANSFER_OUT;
import static org.userapi.dto.TransactionType.COMPLETED;
import static org.userapi.dto.TransactionType.CREATED;

@Service
@RequiredArgsConstructor
public class TransferRequestServiceImpl implements TransferRequestService {

  private final TransactionRepository transactionRepository;
  private final TransferRequestRepository transferRequestRepository;
  private final PaymentRequestRepository paymentRequestRepository;
  private final TransactionalOperator transactionalOperator;
  private final WalletRepository walletRepository;
  private final WalletTypeRepository walletTypeRepository;

  @Override
  public Mono<TransferRequestSaveDto> save(TransferRequestSaveDto transferRequestSaveDto) {
    return transactionalOperator.transactional(
      walletRepository.findByWalletUid(transferRequestSaveDto.getPaymentRequestTo().getWalletUid())
        .flatMap(walletTo -> walletTypeRepository.findById(walletTo.getWalletTypeId())
          .flatMap(walletTypeTo -> walletRepository.findByWalletUid(transferRequestSaveDto.getTransactionFrom().getWalletUid())
            .flatMap(walletFrom -> walletTypeRepository.findById(walletFrom.getWalletTypeId())
              .flatMap(walletTypeFrom -> {
                if (walletTypeFrom.getCurrencyCode() == walletTypeTo.getCurrencyCode()) {
                  return paymentRequestRepository.save(transferRequestSaveDto.getPaymentRequestFrom())
                    .flatMap(paymentRequestFrom -> paymentRequestRepository.save(transferRequestSaveDto.getPaymentRequestTo())
                      .flatMap(paymentRequestTo -> {
                        transferRequestSaveDto.getTransactionTo().setPaymentRequestUid(paymentRequestTo.getUid());
                        transferRequestSaveDto.getTransactionFrom().setPaymentRequestUid(paymentRequestFrom.getUid());
                        transferRequestSaveDto.getTransactionFrom().setWalletName(walletFrom.getName());
                        transferRequestSaveDto.getTransactionTo().setWalletName(walletTo.getName());
                        transferRequestSaveDto.getTransactionTo().setCurrencyCode(walletTypeTo.getCurrencyCode());
                        transferRequestSaveDto.getTransactionFrom().setCurrencyCode(walletTypeFrom.getCurrencyCode());
                        return Mono.zip(transactionRepository.save(transferRequestSaveDto.getTransactionTo()),
                                        transactionRepository.save(transferRequestSaveDto.getTransactionFrom()))
                          .flatMap(tuple -> {
                            transferRequestSaveDto.getTransferRequest().setPaymentRequestUidTo(paymentRequestTo.getUid());
                            transferRequestSaveDto.getTransferRequest().setPaymentRequestUidFrom(paymentRequestFrom.getUid());
                            return transferRequestRepository.save(transferRequestSaveDto.getTransferRequest());
                          });
                      }));
                } else {
                  return Mono.error(new RuntimeException());
                }
              })
              .switchIfEmpty(Mono.error(new RuntimeException())))
            .switchIfEmpty(Mono.error(new RuntimeException())))
          .switchIfEmpty(Mono.error(new RuntimeException())))
        .then(Mono.just(transferRequestSaveDto))
        .switchIfEmpty(Mono.error(new RuntimeException()))
    );
  }

  @Override
  public Mono<Boolean> confirm(UUID transferRequestUid) {
    return transactionalOperator.transactional(
      transferRequestRepository.findByUid(transferRequestUid)
        .flatMap(transferRequest ->
                   Mono.zip(
                     transactionRepository.findByPaymentRequestUid(transferRequest.getPaymentRequestUidFrom()),
                     transactionRepository.findByPaymentRequestUid(transferRequest.getPaymentRequestUidTo())
                   )
        )
        .flatMap(transactions -> {
          if (transactions.getT1().getState() == TRANSFER_OUT && transactions.getT1().getType() == CREATED &&
              transactions.getT2().getState() == TRANSFER_IN && transactions.getT2().getType() == CREATED) {
            return Mono.zip(walletRepository.findByWalletUid(transactions.getT1().getWalletUid()),
                            walletRepository.findByWalletUid(transactions.getT2().getWalletUid()))
              .flatMap(wallets -> Mono.zip(walletTypeRepository.findById(wallets.getT1().getWalletTypeId()),
                                           walletTypeRepository.findById(wallets.getT2().getWalletTypeId()))
                .flatMap(walletTypes -> {
                  if (walletTypes.getT1().getCurrencyCode() == transactions.getT1().getCurrencyCode() &&
                      walletTypes.getT2().getCurrencyCode() == transactions.getT2().getCurrencyCode()) {
                    return Mono.zip(
                        paymentRequestRepository.findByUid(transactions.getT1().getPaymentRequestUid()),
                        paymentRequestRepository.findByUid(transactions.getT2().getPaymentRequestUid())
                      )
                      .flatMap(payments -> {
                        BigDecimal amountOut;
                        BigDecimal amountIn;
                        if (payments.getT1().getCryptoWalletUid() != null && payments.getT1().getAmountInCrypto() != null) {
                          amountOut = payments.getT1().getAmountInCrypto();
                        } else {
                          amountOut = new BigDecimal(transactions.getT1().getRawAmount());
                        }
                        if (payments.getT1().getCryptoWalletUid() != null && payments.getT1().getAmountInCrypto() != null) {
                          amountIn = payments.getT2().getAmountInCrypto();
                        } else {
                          amountIn = new BigDecimal(transactions.getT2().getRawAmount());
                        }
                        if (wallets.getT1().getBalance().compareTo(amountOut) > 0) {
                          wallets.getT1().setBalance(wallets.getT1().getBalance().subtract(amountOut));
                          wallets.getT2().setBalance(wallets.getT2().getBalance().add(amountIn));
                          transactions.getT1().setType(COMPLETED);
                          transactions.getT2().setType(COMPLETED);
                          return Mono.zip(transactionRepository.save(transactions.getT1()),
                                          transactionRepository.save(transactions.getT2()),
                                          walletRepository.save(wallets.getT1()),
                                          walletRepository.save(wallets.getT2()))
                            .then(Mono.just(Boolean.TRUE));
                        } else {
                          return Mono.error(new RuntimeException());
                        }
                      });
                  } else {
                    return Mono.error(new RuntimeException());
                  }
                }));
          }
          return Mono.error(new RuntimeException());
        })
    );
  }
}
