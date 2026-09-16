package com.hadinawa.leasing_management_api.util;

import com.hadinawa.leasing_management_api.model.entities.InstallmentSchedule;
import com.hadinawa.leasing_management_api.model.entities.InstallmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class PaymentAllocatorTest {
    List<InstallmentSchedule> installmentSchedules;
    BigDecimal paymentAmount;
    PaymentAllocator paymentAllocator;

    @BeforeEach
    void setUp() {
        paymentAllocator = new PaymentAllocator();
        installmentSchedules = new ArrayList<>();
        InstallmentSchedule a = new InstallmentSchedule();
        a.setTotalAmount(BigDecimal.valueOf(1000000));
        a.setAmountPaid(BigDecimal.ZERO);
        InstallmentSchedule b = new InstallmentSchedule();
        b.setTotalAmount(BigDecimal.valueOf(1000000));
        b.setAmountPaid(BigDecimal.valueOf(500000));
        InstallmentSchedule c = new InstallmentSchedule();
        c.setTotalAmount(BigDecimal.valueOf(500000));
        c.setAmountPaid(BigDecimal.ZERO);
        installmentSchedules.add(a);
        installmentSchedules.add(b);
        installmentSchedules.add(c);
    }

    @Test
    void exactPayment() {
        paymentAmount = BigDecimal.valueOf(2000000);
        paymentAllocator.allocation(installmentSchedules, paymentAmount);

        assertEquals(InstallmentStatus.PAID, installmentSchedules.get(0).getStatus());
        assertEquals(InstallmentStatus.PAID, installmentSchedules.get(1).getStatus());
        assertEquals(InstallmentStatus.PAID, installmentSchedules.get(2).getStatus());
    }

    @Test
    void halfPayment() {
        paymentAmount = BigDecimal.valueOf(1000000);
        BigDecimal remaining = paymentAllocator.allocation(installmentSchedules,paymentAmount);
        assertEquals(InstallmentStatus.PAID, installmentSchedules.get(0).getStatus());
        assertNull(installmentSchedules.get(1).getStatus()); // untouched — b was never assigned a status in setUp
        assertEquals(0, installmentSchedules.get(1).getAmountPaid().compareTo(BigDecimal.valueOf(500000))); // unchanged from setUp
    }

    @Test
    void overPayment() {
        paymentAmount = BigDecimal.valueOf(3000000);
        BigDecimal remaining = paymentAllocator.allocation(installmentSchedules,paymentAmount);
        assertEquals(1,remaining.compareTo(BigDecimal.ZERO));
    }
}