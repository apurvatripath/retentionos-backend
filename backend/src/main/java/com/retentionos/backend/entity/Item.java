package com.retentionos.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private BigDecimal gstRate;

    @ManyToOne
    @JoinColumn(name = "business_id")
    @JsonIgnore
    private Business business;

    private LocalDateTime createdAt;
}
