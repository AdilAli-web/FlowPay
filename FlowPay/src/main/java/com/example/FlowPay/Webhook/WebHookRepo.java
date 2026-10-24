package com.example.FlowPay.Webhook;

import com.example.FlowPay.Webhook.WebHook;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface WebHookRepo extends JpaRepository<WebHook,String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from WebHook e where e.id=:id")
    Optional<WebHook> lockById(@Param("id") String id);

    List<WebHook> findByMerchantIdOrderByCreatedAtDesc(String m);

    List<WebHook> findTop50ByStatusAndNextAttemptAtBeforeOrderByCreatedAtAsc(String status, Instant now);

}
