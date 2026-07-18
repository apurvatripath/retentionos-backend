package com.retentionos.backend.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.retentionos.backend.entity.BusinessType;
import com.retentionos.backend.entity.Customer;
import com.retentionos.backend.exception.ResourceNotFoundException;
import com.retentionos.backend.repository.CustomerRepository;
import com.retentionos.backend.dto.CsvImportResponse;
import com.retentionos.backend.dto.CustomerSignupResponse;
import com.retentionos.backend.dto.RetentionMessageResponse;
import com.retentionos.backend.dto.SendRetentionMessageResponse;
import com.retentionos.backend.entity.Business;
import com.retentionos.backend.repository.BusinessRepository;
import com.retentionos.backend.dto.DashboardResponse;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final AiMessageService aiMessageService;
    private final WhatsAppService whatsAppService;

    public Customer createCustomer(long businessId, Customer customer) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        customer.setBusiness(business);
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }
    public List<Customer> getInactiveCustomers(Long businessId) {
    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

    int inactiveDays = switch (business.getBusinessType()) {
        case RESTAURANT -> 10;
        case GYM -> 15;
        case SALON -> 30;
        case RETAIL -> 30;
        case OTHER -> 30;
    };

    LocalDate cutoffDate = LocalDate.now().minusDays(inactiveDays);

    if (business.getBusinessType() == BusinessType.RETAIL) {
        return customerRepository.findByBusinessIdAndLastPurchaseDateBefore(businessId, cutoffDate);
    }
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
                    buildRetentionMessage(customer)
            ))
            .toList();
}

private String buildRetentionMessage(Customer customer) {
    Business business = customer.getBusiness();

    String aiMessage = aiMessageService.generateRetentionMessage(
            customer.getName(),
            business.getName(),
            business.getBusinessType(),
            resolveDaysInactive(customer, business.getBusinessType())
    );

    if (aiMessage != null) {
        return aiMessage;
    }

    return "Hi " + customer.getName()
            + ", we haven't seen you at "
            + business.getName()
            + " for a while. Visit us again soon!";
}

public SendRetentionMessageResponse sendRetentionMessage(Long businessId, Long customerId) {
    Customer customer = customerRepository.findByIdAndBusinessId(customerId, businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    String message = buildRetentionMessage(customer);
    boolean sent = whatsAppService.sendTextMessage(customer.getPhone(), message);

    return new SendRetentionMessageResponse(sent, message);
}

private long resolveDaysInactive(Customer customer, BusinessType businessType) {
    LocalDate lastActivityDate = (businessType == BusinessType.RETAIL)
            ? customer.getLastPurchaseDate()
            : customer.getLastVisitDate();

    if (lastActivityDate == null) {
        return 0;
    }
    return ChronoUnit.DAYS.between(lastActivityDate, LocalDate.now());
}
public DashboardResponse getDashboard(Long businessId) {
    long totalCustomers = customerRepository.findByBusinessId(businessId).size();
    long inactiveCustomers = getInactiveCustomers(businessId).size();
    long activeCustomers = totalCustomers - inactiveCustomers;

    return new DashboardResponse(
            totalCustomers,
            activeCustomers,
            inactiveCustomers
    );
}
public List<Customer> getExpiringMemberships(Long businessId) {
    LocalDate today = LocalDate.now();
    LocalDate nextSevenDays = today.plusDays(7);

    return customerRepository.findByBusinessIdAndMembershipExpiryDateBetween(
            businessId,
            today,
            nextSevenDays
    );
}

    public CsvImportResponse importCustomers(Long businessId, MultipartFile file) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        int imported = 0;
        List<String> errors = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : csvParser) {
                try {
                    Customer customer = parseCustomerRecord(record, business);
                    customerRepository.save(customer);
                    imported++;
                } catch (Exception e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }

        return new CsvImportResponse(imported, errors.size(), errors);
    }

    private Customer parseCustomerRecord(CSVRecord record, Business business) {
        String name = record.isMapped("name") ? record.get("name") : null;
        String phone = record.isMapped("phone") ? record.get("phone") : null;

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Missing required field: name");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Missing required field: phone");
        }

        Customer customer = new Customer();
        customer.setName(name.trim());
        customer.setPhone(phone.trim());
        customer.setBusiness(business);
        customer.setJoinDate(LocalDate.now());
        customer.setCreatedAt(LocalDateTime.now());

        customer.setLastVisitDate(parseCsvDate(record, "lastVisitDate"));
        customer.setLastPurchaseDate(parseCsvDate(record, "lastPurchaseDate"));
        customer.setMembershipExpiryDate(parseCsvDate(record, "membershipExpiryDate"));

        return customer;
    }

    private LocalDate parseCsvDate(CSVRecord record, String columnName) {
        if (!record.isMapped(columnName)) {
            return null;
        }
        String value = record.get(columnName);
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid " + columnName + ": " + value);
        }
    }

    public CustomerSignupResponse signupCustomer(Long businessId, String name, String phone) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        return customerRepository.findByPhoneAndBusinessId(phone, businessId)
                .map(customer -> {
                    applyTodayActivityDate(customer, business.getBusinessType());
                    return new CustomerSignupResponse(customerRepository.save(customer), true);
                })
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setPhone(phone);
                    customer.setJoinDate(LocalDate.now());
                    applyTodayActivityDate(customer, business.getBusinessType());
                    customer.setBusiness(business);
                    customer.setCreatedAt(LocalDateTime.now());
                    return new CustomerSignupResponse(customerRepository.save(customer), false);
                });
    }

    private void applyTodayActivityDate(Customer customer, BusinessType businessType) {
        LocalDate today = LocalDate.now();
        if (businessType == BusinessType.RETAIL) {
            customer.setLastPurchaseDate(today);
        } else {
            customer.setLastVisitDate(today);
        }
    }

}