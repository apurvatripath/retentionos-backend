package com.retentionos.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.retentionos.backend.entity.Customer;
import com.retentionos.backend.repository.CustomerRepository;
import com.retentionos.backend.dto.RetentionMessageResponse;
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
    public List<Customer> getInactiveCustomers(Long businessId) {
    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

    int inactiveDays = switch (business.getBusinessType()) {
        case RESTAURANT -> 10;
        case GYM -> 15;
        case SALON -> 30;
        case OTHER -> 30;
    };

    LocalDate cutoffDate = LocalDate.now().minusDays(inactiveDays);

    return customerRepository.findByBusinessIdAndLastVisitDateBefore(businessId, cutoffDate);
}
    public List<Customer> getCustomersByBusiness(Long businessId) {
    return customerRepository.findByBusinessId(businessId);
}

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
 public List<RetentionMessageResponse> generateRetentionMessages(Long businessId) {

    List<Customer> inactiveCustomers = getInactiveCustomers(businessId);

    return inactiveCustomers.stream()
            .map(customer -> new RetentionMessageResponse(
                    customer.getName(),
                    customer.getPhone(),
                    "Hi " + customer.getName()
                            + ", we haven't seen you at "
                            + customer.getBusiness().getName()
                            + " for a while. Visit us again soon!"
            ))
            .toList();
}

}