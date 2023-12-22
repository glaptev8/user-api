package org.userapi.dto;

public enum TransactionState {
  ROLLING_RESERVE,
  SETTLEMENT_IN,
  FEE_REFUND,
  SETTLEMENT_OUT,
  TRANSFER_OUT,
  TRANSFER_IN,
  WITHDRAWAL,
  OWN_TRANSFER,
  TOP_UP
}
