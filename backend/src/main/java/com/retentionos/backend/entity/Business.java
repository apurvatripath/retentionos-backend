package com.retentionos.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ownerName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    private String city;
    private String businessState;

    private LocalDateTime trialStartDate;
    private LocalDateTime trialEndDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "business")
    @JsonIgnore
    private List<Customer> customers;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String authToken;

    private Integer messagesSent;
}