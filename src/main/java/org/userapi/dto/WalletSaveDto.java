package org.userapi.dto;

import org.userapi.entity.Wallet;
import org.userapi.entity.WalletStatusHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WalletSaveDto {
  private Wallet wallet;
  private WalletStatusHistory walletStatusHistory;
}
