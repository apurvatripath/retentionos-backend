package com.retentionos.backend.repository;

import java.util.List;

import com.retentionos.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByBusinessId(Long businessId);
}