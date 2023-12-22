package org.userapi.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.userapi.dto.PaymentRequestStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("payment_requests")
public class PaymentRequest {
  @Id
  private UUID uid;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private UUID profileUid;
  private UUID walletUid;
  private Integer amountGross;
  private Integer fee;
  private PaymentRequestStatus status;
  private BigDecimal percentage;
  private Integer fixedAmount;
  private String option;
  private Integer scale;
  private String comment;
  private Long cryptoCallbackId;
  private BigDecimal amountInCrypto;
  private UUID providerTransactionUid;
  private String providerTransactionId;
  private Long paymentMethodId;
  private UUID cryptoWalletUid;
}

