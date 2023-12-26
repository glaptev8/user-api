package org.userapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("transfer_requests")
public class TransferRequest {
  @Id
  private UUID uid;
  private LocalDateTime createdAt;
  private BigDecimal systemRate;
  private UUID paymentRequestUidFrom;
  private UUID paymentRequestUidTo;
}
