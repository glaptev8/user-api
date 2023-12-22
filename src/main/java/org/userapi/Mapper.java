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
import org.userapi.dto.WalletTypeSaveDto;
import org.userapi.entity.PaymentRequest;
import org.userapi.entity.TopUpRequest;
import org.userapi.entity.Transaction;

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

//  public WalletTypeSaveDto walletResponseDtoToSaveDto(Wa);
}
