package com.retentionos.backend.controller;

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
}