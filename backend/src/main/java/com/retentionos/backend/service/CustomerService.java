package com.retentionos.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.retentionos.backend.entity.Customer;
import com.retentionos.backend.repository.CustomerRepository;
import com.retentionos.backend.entity.Business;
import com.retentionos.backend.repository.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;

    public Customer createCustomer(long businessId, Customer customer) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));
        customer.setBusiness(business);
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }
    public List<Customer> getCustomersByBusiness(Long businessId) {
    return customerRepository.findByBusinessId(businessId);
}

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}