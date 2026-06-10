package com.retentionos.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.retentionos.backend.entity.Customer;
import com.retentionos.backend.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/businesses")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

  @PostMapping("/{businessId}/customers")
public Customer createCustomer(
        @PathVariable Long businessId,
        @RequestBody Customer customer
) {
    return customerService.createCustomer(businessId, customer);
}

  @GetMapping("/{businessId}/customers")
public List<Customer> getCustomersByBusiness(@PathVariable Long businessId) {
    return customerService.getCustomersByBusiness(businessId);
}
}