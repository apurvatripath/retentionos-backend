package com.retentionos.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private LocalDate joinDate;

    private LocalDate lastVisitDate;

    private LocalDate membershipExpiryDate;

    private String status;

    private String notes;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
