package org.userapi.dto;

import org.userapi.entity.PaymentRequest;
import org.userapi.entity.Transaction;
import org.userapi.entity.TransferRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TransferRequestSaveDto {
  private TransferRequest transferRequest;
  private PaymentRequest paymentRequestTo;
  private PaymentRequest paymentRequestFrom;
  private Transaction transactionTo;
  private Transaction transactionFrom;
}
