package com.example.FlowPay.transaction;

import com.example.FlowPay.refund.Refund;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LedgerEntryRepo extends JpaRepository<Leger,String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Leger w WHERE w.id = :id")
    Optional<Leger> lockById(@Param("id") String id);

    List<Leger> findByMerchantIdOrderByCreatedAtDesc(String merchantId);


}
