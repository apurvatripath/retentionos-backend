package com.retentionos.backend.repository;

import com.retentionos.backend.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Long> {
}