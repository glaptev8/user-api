package org.userapi;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.userapi.dto.PaymentRequestStatus;
import org.userapi.dto.TopUpRequestDto;
import org.userapi.dto.TopUpResponseDto;
import org.userapi.dto.TopUpSaveDto;
import org.userapi.dto.TransactionState;
import org.userapi.dto.TransactionType;
import org.userapi.dto.TransferRequestDto;
import org.userapi.dto.TransferRequestSaveDto;
import org.userapi.dto.TransferResponseDto;
import org.userapi.dto.TransferStatus;
import org.userapi.dto.WithdrawalRequestDto;
import org.userapi.dto.WithdrawalResponseDto;
import org.userapi.dto.WithdrawalSaveDto;
import org.userapi.entity.PaymentRequest;
import org.userapi.entity.TopUpRequest;
import org.userapi.entity.Transaction;
import org.userapi.entity.TransferRequest;
import org.userapi.entity.WithdrawalRequest;

@Service
public class Mapper {
  public TopUpSaveDto topUpResponseDtoToSaveDto(TopUpRequestDto topUpResponseDto, UUID profileUuid) {
    var topUpRequest = TopUpRequest.
      builder()
      .provider("test")
      .build();
    var paymentRequest = PaymentRequest.builder()
      .amountGross(topUpResponseDto.getAmountToGross())
      .fee(topUpResponseDto.getToFee())
      .profileUid(profileUuid)
      .walletUid(UUID.fromString(topUpResponseDto.getWalletUidTo()))
      .comment(topUpResponseDto.getComment())
      .fixedAmount(topUpResponseDto.getAmountToNet())
      .option("AND")
//      .cryptoWalletUid(UUID.fromString(topUpResponseDto.getCryptoWalletUid()))
      .percentage(BigDecimal.ZERO)
      .scale(0)
      .status(PaymentRequestStatus.CREATED)
      .paymentMethodId(Long.valueOf(topUpResponseDto.getPaymentMethodId()))
      .amountInCrypto(topUpResponseDto.getAmountInCrypto())
      .build();
    var transaction = Transaction.builder()
      .amountInUsd(0)
      .fee(topUpResponseDto.getToFee())
      .profileUid(profileUuid)
      .refundFee(0L)
      .rawAmount(topUpResponseDto.getAmountToGross())
      .walletUid(UUID.fromString(topUpResponseDto.getWalletUidTo()))
      .state(TransactionState.TOP_UP)
      .type(TransactionType.CREATED)
      .balanceOperationAmount(0)
      .build();

    return new TopUpSaveDto(topUpRequest, paymentRequest, transaction);
  }

  public TopUpResponseDto topUpSavedDtoToTopUpResponseDto(TopUpSaveDto topUpSaveDto) {
    return TopUpResponseDto.builder()
      .amountToNet(topUpSaveDto.getPaymentRequest().getFixedAmount())
      .toFee(topUpSaveDto.getPaymentRequest().getFee())
      .build();
  }

  public WithdrawalSaveDto withdrawalSaveDto(WithdrawalRequestDto withdrawalRequestDto, UUID profileUuid) {
    var withdrawalRequest = WithdrawalRequest.builder()
      .token("empty")
      .provider("test")
      .build();
    var paymentRequest = PaymentRequest.builder()
      .amountGross(withdrawalRequestDto.getAmountFromGross())
      .fee(withdrawalRequestDto.getFromFee())
      .profileUid(profileUuid)
      .walletUid(UUID.fromString(withdrawalRequestDto.getWalletUidFrom()))
      .comment(withdrawalRequestDto.getComment())
      .fixedAmount(withdrawalRequestDto.getAmountFromNet())
      .option("AND")
      .cryptoWalletUid(UUID.fromString(withdrawalRequestDto.getCryptoWalletUid()))
      .percentage(BigDecimal.ZERO)
      .scale(0)
      .status(PaymentRequestStatus.CREATED)
//      .paymentMethodId(Long.valueOf(withdrawalRequestDto))
      .amountInCrypto(withdrawalRequestDto.getAmountInCrypto())
      .build();
    var transaction = Transaction.builder()
      .amountInUsd(0)
      .fee(withdrawalRequestDto.getFromFee())
      .profileUid(profileUuid)
      .refundFee(0L)
      .rawAmount(withdrawalRequestDto.getAmountFromGross())
      .walletUid(UUID.fromString(withdrawalRequestDto.getWalletUidFrom()))
      .state(TransactionState.WITHDRAWAL)
      .type(TransactionType.CREATED)
      .balanceOperationAmount(0)
      .build();

    return WithdrawalSaveDto.builder()
      .withdrawalRequest(withdrawalRequest)
      .transaction(transaction)
      .paymentRequest(paymentRequest)
      .build();
  }

  public WithdrawalResponseDto withdrawalToResponseDto(WithdrawalSaveDto withdrawalRequestDto) {
    return WithdrawalResponseDto
      .builder()
      .paymentRequestUid(withdrawalRequestDto.getWithdrawalRequest().getPaymentRequestUid())
      .status(withdrawalRequestDto.getTransaction().getType() == TransactionType.COMPLETED ? TransferStatus.SUCCESS : TransferStatus.ERROR)
      .build();
  }

  public TransferRequestSaveDto transferRequestToSaveDto(TransferRequestDto transferRequestDto, UUID profileUuid) {
    Transaction transactionTo = Transaction.builder()
      .amountInUsd(0)
      .fee(transferRequestDto.getToFee())
      .profileUid(profileUuid)
      .refundFee(0L)
      .rawAmount(transferRequestDto.getAmountToGross())
      .walletUid(UUID.fromString(transferRequestDto.getWalletUidTo()))
      .state(TransactionState.TRANSFER_IN)
      .type(TransactionType.CREATED)
      .balanceOperationAmount(0)
      .build();
    var transactionFrom = Transaction.builder()
      .amountInUsd(0)
      .fee(transferRequestDto.getFromFee())
      .profileUid(profileUuid)
      .refundFee(0L)
      .rawAmount(transferRequestDto.getAmountFromGross())
      .walletUid(UUID.fromString(transferRequestDto.getWalletUidFrom()))
      .state(TransactionState.TRANSFER_OUT)
      .type(TransactionType.CREATED)
      .balanceOperationAmount(0)
      .build();
    var paymentRequestTo = PaymentRequest.builder()
      .amountGross(transferRequestDto.getAmountToGross())
      .fee(transferRequestDto.getToFee())
      .profileUid(profileUuid)
      .walletUid(UUID.fromString(transferRequestDto.getWalletUidTo()))
      .comment(transferRequestDto.getComment())
      .fixedAmount(transferRequestDto.getAmountToNet())
      .option("AND")
      .percentage(BigDecimal.ZERO)
      .scale(0)
      .status(PaymentRequestStatus.CREATED)
      .build();
    var paymentRequestFrom = PaymentRequest.builder()
      .amountGross(transferRequestDto.getAmountFromGross())
      .fee(transferRequestDto.getFromFee())
      .profileUid(profileUuid)
      .walletUid(UUID.fromString(transferRequestDto.getWalletUidFrom()))
      .comment(transferRequestDto.getComment())
      .fixedAmount(transferRequestDto.getAmountFromNet())
      .option("AND")
      .percentage(BigDecimal.ZERO)
      .scale(0)
      .status(PaymentRequestStatus.CREATED)
      .build();
    var transferRequest = TransferRequest.builder()
      .systemRate(new BigDecimal(2))
      .build();
    return TransferRequestSaveDto.builder()
      .paymentRequestTo(paymentRequestTo)
      .paymentRequestFrom(paymentRequestFrom)
      .transactionFrom(transactionFrom)
      .transactionTo(transactionTo)
      .transferRequest(transferRequest)
      .build();
  }

  public TransferResponseDto transferResponseDtoFromSaved(TransferRequestSaveDto transferRequestSaveDto) {
    return TransferResponseDto.builder()
      .amountFromNet(transferRequestSaveDto.getPaymentRequestFrom().getFixedAmount())
      .amountToNet(transferRequestSaveDto.getPaymentRequestTo().getFixedAmount())
      .amountFromGross(transferRequestSaveDto.getPaymentRequestFrom().getAmountGross())
      .amountToGross(transferRequestSaveDto.getPaymentRequestTo().getAmountGross())
      .fromFee(transferRequestSaveDto.getPaymentRequestFrom().getFee())
      .toFee(transferRequestSaveDto.getPaymentRequestTo().getFee())
      .walletCurrencyFrom(transferRequestSaveDto.getTransactionFrom().getCurrencyCode().toString())
      .walletCurrencyTo(transferRequestSaveDto.getTransactionTo().getCurrencyCode().toString())
      .status(TransferStatus.SUCCESS)
      .systemRate(transferRequestSaveDto.getTransferRequest().getSystemRate().intValue())
      .transferRequestUid(transferRequestSaveDto.getTransferRequest().getUid())
      .build();
  }
//  public WalletTypeSaveDto walletResponseDtoToSaveDto(Wa);
}
