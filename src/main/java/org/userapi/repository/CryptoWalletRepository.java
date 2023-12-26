package org.userapi.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.userapi.entity.CryptoWallet;

public interface CryptoWalletRepository extends R2dbcRepository<CryptoWallet, Long>  {
}
