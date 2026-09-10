package com.hadinawa.leasing_management_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "installment_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String installmentNumber;

    private Date dueDate;

    private BigDecimal principalPortion;

    private BigDecimal interestPortion;

    private BigDecimal amountPaid;

    private InstallmentStatus status;

    @Transient
    public BigDecimal getRemainingAmount(){
        return null;
    }
}
