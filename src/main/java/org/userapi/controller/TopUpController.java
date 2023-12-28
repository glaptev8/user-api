package org.userapi.controller;

import java.util.UUID;

import org.leantech.common.dto.TopUpRequestDto;
import org.leantech.common.dto.TopUpResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.userapi.Mapper;
import org.userapi.service.api.TopUpRequestService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class TopUpController {

  private final Mapper mapper;
  private final TopUpRequestService topUpRequestService;

  @PostMapping("/topup")
  public Mono<TopUpResponseDto> topUp(@RequestBody TopUpRequestDto topUpRequestDto,
                                      @RequestHeader(name = "PROFILE_UUID") UUID profileUuid) {
    var topUpSaveDto = mapper.topUpResponseDtoToSaveDto(topUpRequestDto, profileUuid);
    return topUpRequestService.save(topUpSaveDto)
      .map(mapper::topUpSavedDtoToTopUpResponseDto);
  }
}
