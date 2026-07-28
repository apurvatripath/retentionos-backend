package com.retentionos.backend.controller;

import com.retentionos.backend.dto.SetPasswordRequest;
import com.retentionos.backend.entity.Business;
import com.retentionos.backend.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public Business createBusiness(@RequestBody Business business) {
        return businessService.createBusiness(business);
    }

    @GetMapping
    public List<Business> getAllBusinesses() {
        return businessService.getAllBusinesses();
    }
    @GetMapping("/trial-expiring")
public List<Business> getTrialExpiringBusinesses() {
    return businessService.getTrialExpiringBusinesses();
}
@GetMapping("/subscription-expired")
public List<Business> getSubscriptionExpiredBusinesses() {
    return businessService.getSubscriptionExpiredBusinesses();
}

    @PostMapping("/{id}/set-password")
    public Business setPassword(@PathVariable Long id, @RequestBody SetPasswordRequest request) {
        return businessService.setPassword(id, request.phone(), request.password());
    }
}