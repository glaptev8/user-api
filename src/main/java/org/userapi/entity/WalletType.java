package org.userapi.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.userapi.dto.Currency;
import org.userapi.dto.ProfileType;
import org.userapi.dto.WalletStatus;
import org.userapi.dto.WalletTypeStatus;

import lombok.Data;

@Data
@Table("wallet_types")
public class WalletType {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String name;
  private Currency currencyCode;
  private WalletTypeStatus status;
  private LocalDateTime archivedAt;
  private ProfileType profileType;
  private String creator;
  private String modifier;
}
