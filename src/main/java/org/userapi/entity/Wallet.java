package org.userapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.userapi.dto.WalletStatus;

import lombok.Data;

@Data
@Table("wallets")
public class Wallet {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String name;
  private Long walletTypeId;
  private UUID profileUid;
  private WalletStatus status;
  private BigDecimal balance;
  private LocalDateTime archivedAt;
  private UUID walletUid;
}
