package org.userapi.service.api;

import java.util.UUID;

import org.userapi.dto.WithdrawalSaveDto;

import reactor.core.publisher.Mono;

public interface WithdrawalService {
  Mono<WithdrawalSaveDto> save(WithdrawalSaveDto withdrawalDto);

  Mono<Boolean> confirm(UUID paymentRequestUid);
}
