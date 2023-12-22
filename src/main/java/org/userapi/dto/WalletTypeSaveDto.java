package org.userapi.dto;

import org.userapi.entity.WalletType;
import org.userapi.entity.WalletTypeStatusHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WalletTypeSaveDto {
  private WalletType walletType;
  private WalletTypeStatusHistory walletTypeStatusHistory;
}
