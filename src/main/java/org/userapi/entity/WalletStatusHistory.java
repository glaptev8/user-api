package org.userapi.entity;

import lombok.Builder;
import lombok.Data;

import org.leantech.common.dto.ProfileType;
import org.leantech.common.dto.WalletStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("wallet_status_history")
public class WalletStatusHistory {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private UUID walletUid;
  private UUID changedByUserUid;
  private ProfileType changedByProfileType;
  private String reason;
  private WalletStatus fromStatus;
  private String comment;
  private WalletStatus toStatus;
}
