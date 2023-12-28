package org.userapi.entity;

import lombok.Data;

import org.leantech.common.dto.WalletTypeStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("wallet_types_status_history")
public class WalletTypeStatusHistory {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private Long walletTypeId;
  private UUID changedByUserUid;
  private String changedByProfileType;
  private String reason;
  private WalletTypeStatus fromStatus;
  private String comment;
  private WalletTypeStatus toStatus;
}

