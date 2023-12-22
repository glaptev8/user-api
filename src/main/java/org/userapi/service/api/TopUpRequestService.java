package org.userapi.service.api;

import org.userapi.dto.TopUpRequestDto;
import org.userapi.dto.TopUpResponseDto;
import org.userapi.dto.TopUpSaveDto;

import reactor.core.publisher.Mono;

public interface TopUpRequestService {
  Mono<TopUpSaveDto> save(TopUpSaveDto topUpResponseDto);
}
