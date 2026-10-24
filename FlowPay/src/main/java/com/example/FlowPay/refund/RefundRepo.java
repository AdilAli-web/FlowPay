package com.example.FlowPay.refund;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefundRepo extends JpaRepository<Refund,String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Refund w WHERE w.id = :id")
    Optional<Refund> lockById(@Param("id") String id);

    Optional<Refund> findByMerchantIdAndIdempotencyKey(String m,String k);

    List<Refund>  findByMerchantIdOrderByCreatedAtDesc(String m);

}
