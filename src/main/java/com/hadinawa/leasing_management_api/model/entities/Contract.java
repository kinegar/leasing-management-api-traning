package com.hadinawa.leasing_management_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private String contractNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String collateralDescription;

    private Boolean collateralDocumentHeld;

    private BigDecimal principalAmount;

    private Double annualInterestRatePercent;

    private Integer tenorMonths;

    private Date startDate;

    private ContractStatus status;

    private List<InstallmentSchedule> installments;

    private List<Payment> payments;
}
