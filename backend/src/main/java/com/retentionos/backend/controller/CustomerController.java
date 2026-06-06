package com.retentionos.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.retentionos.backend.entity.Customer;
import com.retentionos.backend.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}