package com.retentionos.backend.service;

import com.retentionos.backend.entity.Business;
import com.retentionos.backend.entity.SubscriptionStatus;
import com.retentionos.backend.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    public Business createBusiness(Business business) {
        business.setTrialStartDate(LocalDateTime.now());
      business.setTrialEndDate(LocalDateTime.now().plusMonths(1));
        business.setSubscriptionStatus(SubscriptionStatus.TRIAL);
        business.setCreatedAt(LocalDateTime.now());

        return businessRepository.save(business);
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }
    public List<Business> getTrialExpiringBusinesses() {
    LocalDateTime nextSevenDays = LocalDateTime.now().plusDays(7);

    return businessRepository.findBySubscriptionStatusAndTrialEndDateBefore(
            SubscriptionStatus.TRIAL,
            nextSevenDays
    );
}
public List<Business> getSubscriptionExpiredBusinesses() {
    LocalDateTime now = LocalDateTime.now();

    return businessRepository.findBySubscriptionStatusAndTrialEndDateBefore(
            SubscriptionStatus.TRIAL,
            now
    );
}
}
