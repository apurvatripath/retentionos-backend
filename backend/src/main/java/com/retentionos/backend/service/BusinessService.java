package com.retentionos.backend.service;

import com.retentionos.backend.dto.LoginResponse;
import com.retentionos.backend.entity.Business;
import com.retentionos.backend.entity.SubscriptionStatus;
import com.retentionos.backend.exception.ResourceNotFoundException;
import com.retentionos.backend.exception.UnauthorizedException;
import com.retentionos.backend.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

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
            SubscriptionStatus.ACTIVE,
            now
    );
}

public Business setPassword(Long businessId, String phone, String rawPassword) {
    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

    if (phone == null || !phone.equals(business.getPhone())) {
        throw new UnauthorizedException("Phone does not match business records");
    }

    if (business.getPassword() != null) {
        throw new IllegalStateException("Password already set for this business");
    }

    business.setPassword(PASSWORD_ENCODER.encode(rawPassword));
    return businessRepository.save(business);
}

public LoginResponse login(String phone, String rawPassword) {
    Business business = businessRepository.findByPhone(phone)
            .orElseThrow(() -> new UnauthorizedException("Invalid phone or password"));

    if (business.getPassword() == null || !PASSWORD_ENCODER.matches(rawPassword, business.getPassword())) {
        throw new UnauthorizedException("Invalid phone or password");
    }

    String token = UUID.randomUUID().toString();
    business.setAuthToken(token);
    businessRepository.save(business);

    return new LoginResponse(business, token);
}

public Business updateBusinessState(Long businessId, String token, String businessState) {
    requireValidToken(businessId, token);

    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

    business.setBusinessState((businessState == null || businessState.isBlank()) ? null : businessState.trim());
    return businessRepository.save(business);
}

public void requireValidToken(Long businessId, String token) {
    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

    if (token == null || business.getAuthToken() == null || !business.getAuthToken().equals(token)) {
        throw new UnauthorizedException("Invalid or missing auth token");
    }
}
}
