package com.example.FlowPay.Settlement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface SettleRepo extends JpaRepository<Settlement,String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Settlement w WHERE w.id = :id")
    Optional<Settlement> lockById(@Param("id") String id);

    Optional<Settlement> findByMerchantIdAndIdempotencyKey(String merchantId,String key);
    List<Settlement> findByMerchantIdOrderByCreatedAtDesc(String merchantId);

}
