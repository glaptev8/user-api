package org.userapi.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.userapi.dto.ProfileType;
import org.userapi.dto.WalletStatus;

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
