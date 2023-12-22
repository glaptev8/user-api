package org.userapi.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Top up request dto")
public class TopUpRequestDto {
  @Schema(description = "Payment Method")
  private PaymentMethod method;
  @Schema(description = "Internal Payment Method id")
  @JsonProperty("payment_method_id")
  private String paymentMethodId;
  @Schema(description = "Wallet uid destination")
  @JsonProperty("wallet_uid_to")
  private String walletUidTo;
  @Schema(description = "Recipient's gross amount to receive")
  @Min(value = 0)
  @JsonProperty("amount_to_gross")
  private Integer amountToGross;
  @Schema(description = "Recipient's net amount")
  @Min(value = 1)
  @JsonProperty("amount_to_net")
  private Integer amountToNet;
  @Schema(description = "Recipient's fee")
  @Min(value = 0)
  @JsonProperty("to_fee")
  private Integer toFee;
  @Schema(description = "Comment")
  private String comment;
//  @Builder.Default
//  @Schema(description = "Flags", example = "[SUM_WILL_BE_FROZEN]")
//  private List<Flag> flags = new ArrayList<>();
  @Schema(description = "person identity number (CPF, INN, etc)", example = "073.650.323-40", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personal_identity_number")
  private String personalIdentityNumber;
  @Schema(description = "Top up required fields with values")
  @JsonProperty("top_up_required_fields_values")
  private Map<String, Object> topUpRequiredFieldsValues;
  @JsonProperty("crypto_wallet_uid")
  private String cryptoWalletUid;
  @JsonProperty("amount_in_crypto")
  private BigDecimal amountInCrypto;
}