package org.userapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("top_up_requests")
public class TopUpRequest {
  @Id
  private UUID uid;
  private LocalDateTime createdAt;
  private String provider;
  private UUID paymentRequestUid;
}

