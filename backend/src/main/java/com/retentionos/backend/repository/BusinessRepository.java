package com.retentionos.backend.repository;

import com.retentionos.backend.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import com.retentionos.backend.entity.SubscriptionStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findBySubscriptionStatusAndTrialEndDateBefore(
        SubscriptionStatus subscriptionStatus,
        LocalDateTime date
);

    Optional<Business> findByPhone(String phone);
}