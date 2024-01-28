package org.userapi.controller;

import java.util.UUID;

import org.leantech.common.dto.WalletDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.userapi.mapper.MapStructMapper;
import org.userapi.service.api.WalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {
  private final WalletService walletService;
  @GetMapping("/{walletUid}")
  public Mono<WalletDto> getWalletByUid(@PathVariable UUID walletUid) {
    log.info("uid {}", walletUid);
    return walletService.getByUuid(walletUid);
  }
}
