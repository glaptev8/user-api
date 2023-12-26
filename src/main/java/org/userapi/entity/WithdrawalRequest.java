package org.userapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("withdrawal_requests")
public class WithdrawalRequest {
  @Id
  private UUID uid;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String provider;
  private String token;
  private UUID paymentRequestUid;
}

