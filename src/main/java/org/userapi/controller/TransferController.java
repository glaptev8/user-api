package org.userapi.controller;

import java.util.UUID;

import org.leantech.common.dto.TransferRequestDto;
import org.leantech.common.dto.TransferResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.userapi.Mapper;
import org.userapi.service.api.TransferRequestService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/transfer")
public class TransferController {
  private final Mapper mapper;
  private final TransferRequestService transferRequestService;

  @PostMapping
  public Mono<TransferResponseDto> transfer(@RequestBody TransferRequestDto transferRequestDto,
                                            @RequestHeader(name = "PROFILE_UUID") UUID profileUuid) {
    return transferRequestService.save(mapper.transferRequestToSaveDto(transferRequestDto, profileUuid))
      .map(mapper::transferResponseDtoFromSaved);
  }

  @GetMapping("/confirm")
  public Mono<Boolean> confirm(@RequestParam UUID transferRequestUid) {
    return transferRequestService.confirm(transferRequestUid);
  }
}
