package com.example.FlowPay.merchant;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MerchantRepo extends JpaRepository<Merchant,String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Merchant> lockById(@Param("id") String id);

    Optional<Merchant> findByApiKeyHash(String hash);
}
