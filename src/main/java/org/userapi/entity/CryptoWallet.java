package org.userapi.entity;

import org.leantech.common.dto.CryptoWalletStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
@Table("crypto_wallets")
public class CryptoWallet {
  @Id
  private UUID uid;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String ticker;
  private String address;
  private String network;
  private UUID profileUid;
  private CryptoWalletStatus status;
  private UUID walletUid;
}
