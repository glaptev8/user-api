package org.userapi.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferResponseDto extends BaseDto {
  @Schema(description = "Transfer status", example = "SUCCESS", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  private TransferStatus status;
  @Schema(description = "Transfer request uid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("transfer_request_uid")
  private UUID transferRequestUid;
  @Schema(description = "Fee amount would be charged from receiver", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("to_fee")
  private Integer toFee;
  @Schema(description = "Fee amount would be charged from sender", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from_fee")
  private Integer fromFee;
  @Schema(description = "Amount would be charged from the sender gross",example = "1050")
  @JsonProperty("amount_from_gross")
  private Integer amountFromGross;
  @Schema(description = "Amount would be charged from the sender net (without fee)",example = "900")
  @JsonProperty("amount_from_net")
  private Integer amountFromNet;
  @Schema(description = "Amount would be received by the receiver gross", example = "900")
  @JsonProperty("amount_to_gross")
  private Integer amountToGross;
  @Schema(description = "Amount would be received by the receiver net (including fee)", example = "850")
  @JsonProperty("amount_to_net")
  private Integer amountToNet;
  @Schema(description = "Currency rate for transfer currencies pair", example = "1843")
  @JsonProperty("system_rate")
  private Integer systemRate;
  @Schema(description = "Sender wallet currency", example = "USD")
  @JsonProperty("wallet_currency_from")
  private String walletCurrencyFrom;
  @Schema(description = "Recipient wallet currency", example = "USD")
  @JsonProperty("wallet_currency_to")
  private String walletCurrencyTo;
}