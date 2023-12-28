package org.userapi.entity;

import lombok.Builder;
import lombok.Data;

import org.leantech.common.dto.TransactionState;
import org.leantech.common.dto.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.leantech.common.dto.Currency;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("transactions")
public class Transaction {
  @Id
  private UUID uid;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private UUID linkedTransaction;
  private UUID profileUid;
  private UUID walletUid;
  private String walletName;
  private Integer balanceOperationAmount;
  private Integer rawAmount;
  private Integer fee;
  private Integer amountInUsd;
  private TransactionType type;
  private TransactionState state;
  private UUID paymentRequestUid;
  private Currency currencyCode;
  private Long refundFee;
}

