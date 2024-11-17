package com.etraveli.cardcostapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "clearing_cost")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClearingCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;
}
