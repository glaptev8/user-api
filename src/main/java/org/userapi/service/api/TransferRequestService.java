package org.userapi.service.api;

import java.util.UUID;

import org.userapi.dto.TransferRequestSaveDto;

import reactor.core.publisher.Mono;

public interface TransferRequestService {
  Mono<TransferRequestSaveDto> save(TransferRequestSaveDto transferRequestSaveDto);

  Mono<Boolean> confirm(UUID transferRequestUid);
}
