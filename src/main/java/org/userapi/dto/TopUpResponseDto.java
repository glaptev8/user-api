package org.userapi.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopUpResponseDto extends BaseDto {
  @JsonProperty("callback_url")
  private String callbackUrl;
  @JsonProperty("error_code")
  private String errorCode;
  private String message;
  private ErrorInfoLevel level;
  @JsonProperty("additional_info")
  private Map<String, Object> additionalInfo;
  @JsonProperty("landing_info")
  private Boolean LandingInfo;
  @JsonProperty("html_string")
  private String htmlString;
  @JsonProperty("to_fee")
  private Integer toFee;
  @JsonProperty("amount_to_gross")
  private Integer amountToGross;
  @JsonProperty("amount_to_net")
  private Integer amountToNet;
  @JsonProperty("is_previous_transaction")
  private boolean isPreviousTransaction;
}
