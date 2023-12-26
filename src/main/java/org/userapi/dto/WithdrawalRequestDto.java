package org.userapi.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Withdrawal request dto")
public class WithdrawalRequestDto {
  @Schema(description = "PaymentProvider")
  private PaymentProviderType provider;
  @Schema(description = "Wallet uid destination")
  private String walletUidFrom;
  @Schema(description = "token")
  private String token;
  @Schema(description = "Sender's gross amount to send")
  @DecimalMin(value = "0")
  private Integer amountFromGross;
  @Schema(description = "Sender's net amount to send")
  @DecimalMin(value = "0")
  private Integer amountFromNet;
  @Schema(description = "Sender's fee")
  @DecimalMin(value = "0")
  private Integer fromFee;
  @Schema(description = "Comment")
  private String comment;
  @JsonProperty("crypto_wallet_uid")
  private String cryptoWalletUid;
  @JsonProperty("amount_in_crypto")
  private BigDecimal amountInCrypto;
  @JsonProperty("crypto_symbol")
  private String ticker;
  @JsonProperty("fiat_symbol")
  private String fiatSymbol;
}