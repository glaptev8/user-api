package org.userapi.dto;

import org.userapi.entity.PaymentRequest;
import org.userapi.entity.TopUpRequest;
import org.userapi.entity.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TopUpSaveDto {
  private TopUpRequest topUpRequest;
  private PaymentRequest paymentRequest;
  private Transaction transaction;
}
