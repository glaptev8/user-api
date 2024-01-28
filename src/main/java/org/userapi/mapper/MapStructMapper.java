package org.userapi.mapper;

import org.leantech.common.dto.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.userapi.entity.Wallet;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  WalletDto walletDto(Wallet wallet);
}
