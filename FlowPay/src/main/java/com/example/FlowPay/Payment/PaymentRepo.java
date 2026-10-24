package com.example.FlowPay.Payment;

import com.example.FlowPay.order.PaymentOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepo  extends JpaRepository<Payment,String> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Payment w WHERE w.id = :id")
    Optional<PaymentOrder> lockById(@Param("id") String id);



    Optional<Payment> findByMerchantIdAndIdempotencyKey(String m,String k);
    List<Payment> findByMerchantIdOrderByCreatedAtDesc(String m);

    boolean existsByOrderIdAndStatusIn(String id, Collection<String> statuses);


    List<Payment> findTop50ByStatusAndScenarioAndResolveAtBeforeOrderByCreateAtAsc(String status, String scenario, Instant now);

}
