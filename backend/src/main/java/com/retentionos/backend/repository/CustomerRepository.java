package com.retentionos.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.retentionos.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByBusinessId(Long businessId);

    Optional<Customer> findByIdAndBusinessId(Long id, Long businessId);

    Optional<Customer> findByPhoneAndBusinessId(String phone, Long businessId);

    long countByBusinessIdAndCheckInCountGreaterThan(Long businessId, int checkInCount);

    List<Customer> findByBusinessIdAndLastVisitDateBefore(Long businessId, LocalDate cutoffDate);
    List<Customer> findByBusinessIdAndLastPurchaseDateBefore(Long businessId, LocalDate cutoffDate);
    List<Customer> findByBusinessIdAndMembershipExpiryDateBetween(
        Long businessId,
        LocalDate startDate,
        LocalDate endDate
);
}