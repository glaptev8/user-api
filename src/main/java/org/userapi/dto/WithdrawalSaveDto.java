package org.userapi.dto;

import org.userapi.entity.PaymentRequest;
import org.userapi.entity.Transaction;
import org.userapi.entity.WithdrawalRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithdrawalSaveDto {
  private WithdrawalRequest withdrawalRequest;
  private PaymentRequest paymentRequest;
  private Transaction transaction;
}
