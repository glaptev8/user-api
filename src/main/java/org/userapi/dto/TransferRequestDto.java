package org.userapi.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Transfer request dto")
public class TransferRequestDto {

  @Schema(description = "Wallet uid from", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Wallet uid from must not be null")
  private String walletUidFrom;

  @Schema(description = "Wallet uid to", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Wallet uid to must not be null")
  private String walletUidTo;

  @Schema(description = "Senders's gross amount to send ")
  @Min(value = 0)
  private Integer amountFromGross;

  @Schema(description = "Recipient's gross amount to receive")
  @Min(value = 0)
  private Integer amountToGross;

  @Schema(description = "Recipient's net amount")
  @Min(value = 0)
  private Integer amountFromNet;

  @Schema(description = "Recipient's net amount")
  @Min(value = 0)
  private Integer amountToNet;

  @Schema(description = "Senders's fee ")
  @Min(value = 0)
  private Integer fromFee;

  @Schema(description = "Recipient's fee")
  @Min(value = 0)
  private Integer toFee;

  @Schema(description = "System rate for current pair", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "System rate must not be null")
  @DecimalMin(value = "0")
  private BigDecimal systemRate;

  @Schema(description = "Comment")
  private String comment;

  @Schema(description = "Flags", example = "[SUM_WILL_BE_FROZEN]")
  private List<Flag> flags = new ArrayList<>();
}