package org.userapi.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.userapi.Mapper;
import org.userapi.dto.WithdrawalRequestDto;
import org.userapi.dto.WithdrawalResponseDto;
import org.userapi.service.api.WithdrawalService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/withdrawal")
public class WithdrawalController {
  private final Mapper mapper;
  private final WithdrawalService withdrawalService;
  @PostMapping
  public Mono<WithdrawalResponseDto> withdrawalResponse(@RequestBody WithdrawalRequestDto withdrawalRequestDto,
                                                        @RequestHeader(name = "PROFILE_UUID") UUID profileUuid) {
    return withdrawalService.save(mapper.withdrawalSaveDto(withdrawalRequestDto, profileUuid))
      .map(mapper::withdrawalToResponseDto);
  }

  @GetMapping("/confirm")
  public Mono<Boolean> withdrawalConfirm(@RequestParam UUID paymentRequestUid) {
    return withdrawalService.confirm(paymentRequestUid);
  }
}
