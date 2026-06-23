package com.retentionos.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.retentionos.backend.dto.DashboardResponse;
import com.retentionos.backend.dto.RetentionMessageResponse;
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
@GetMapping("/{businessId}/customers/inactive")
public List<Customer> getInactiveCustomers(@PathVariable Long businessId) {
    return customerService.getInactiveCustomers(businessId);
}
@GetMapping("/{businessId}/messages")
public List<RetentionMessageResponse> getRetentionMessages(@PathVariable Long businessId) {
    return customerService.generateRetentionMessages(businessId);
}
@GetMapping("/{businessId}/dashboard")
public DashboardResponse getDashboard(@PathVariable Long businessId) {
    return customerService.getDashboard(businessId);
}
@GetMapping("/{businessId}/customers/expiring-memberships")
public List<Customer> getExpiringMemberships(@PathVariable Long businessId) {
    return customerService.getExpiringMemberships(businessId);
}

}