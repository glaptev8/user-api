package org.userapi.entity;

import java.time.LocalDateTime;

import org.leantech.common.dto.Currency;
import org.leantech.common.dto.WalletTypeStatus;
import org.leantech.person.dto.ProfileType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
