package com.hadinawa.leasing_management_api.util;

import com.hadinawa.leasing_management_api.model.record.InstallmentLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstallmentScheduleMathTest {
    /**
     * Tests worth writing before/alongside the implementation:
     *
     * Sum of all principalPortion values equals principal exactly.
     * dueDate of installment n is exactly n months after startDate, for a start date that's the 31st of a month (this is where the drift bug would show up if you got due-date calculation wrong).
     * Tenor of 1 — the single installment should just be principal + one month's interest.
     * 0% interest rate — installments should be principal-only.
     * A case with an ugly, non-dividing principal (e.g. principal = 10,000,000, tenor = 7) to make sure the remainder-absorption actually works.
     */

    BigDecimal principal;
    BigDecimal annualRatePercent;
    int tenorMonths;
    LocalDate startDate;

    @BeforeEach
    void before(){
        principal = new BigDecimal(5000000);
        annualRatePercent = new BigDecimal("0.0575");
        tenorMonths = 12;
        startDate = LocalDate.now();
    }

    @Test
    void sumAllPrincipalPortionEqualPrincipal() {
        InstallmentScheduleMath installmentScheduleMath = new InstallmentScheduleMath();
        List<InstallmentLine> installmentLines =
                installmentScheduleMath.scheduleGenerator(principal,annualRatePercent, tenorMonths, startDate);

        BigDecimal sumAllPrincipalPortion = BigDecimal.ZERO;
        for(InstallmentLine il : installmentLines){
            sumAllPrincipalPortion = sumAllPrincipalPortion.add(il.principalPortion());
        }
        assertEquals(0,sumAllPrincipalPortion.compareTo(principal));
    }

    @Test
    void sumAllPrincipalPortionEqualPrincipalSpecialCase1() {
        principal = BigDecimal.valueOf(5000000);
        annualRatePercent = new BigDecimal("0.13");

        InstallmentScheduleMath installmentScheduleMath = new InstallmentScheduleMath();
        List<InstallmentLine> installmentLines =
                installmentScheduleMath.scheduleGenerator(principal,annualRatePercent, tenorMonths, startDate);

        BigDecimal sumAllPrincipalPortion = BigDecimal.ZERO;
        for(InstallmentLine il : installmentLines){
            sumAllPrincipalPortion = sumAllPrincipalPortion.add(il.principalPortion());
        }
        assertEquals(0,sumAllPrincipalPortion.compareTo(principal));
    }

    @Test
    void dueDateOfInstallment(){
        startDate = LocalDate.of(2026,1,31);
        tenorMonths = 6;
        InstallmentScheduleMath installmentScheduleMath = new InstallmentScheduleMath();
        List<InstallmentLine> installmentLines =
                installmentScheduleMath.scheduleGenerator(principal,annualRatePercent, tenorMonths, startDate);
        LocalDate nDate = installmentLines.get(tenorMonths-1).dueDate();
        LocalDate actual = startDate.plusMonths(tenorMonths);
        assertEquals(nDate.getMonth().getValue(),actual.getMonth().getValue());
    }

    @Test
    void singleTenor(){
        tenorMonths = 1;
        BigDecimal monthlyInterest = principal
                .multiply(annualRatePercent)
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);
        BigDecimal actual = principal.add(monthlyInterest);


        InstallmentScheduleMath installmentScheduleMath = new InstallmentScheduleMath();
        List<InstallmentLine> installmentLines =
                installmentScheduleMath.scheduleGenerator(principal,annualRatePercent, tenorMonths, startDate);

        BigDecimal sumAllPayment = BigDecimal.ZERO;
        for(InstallmentLine il : installmentLines){
            sumAllPayment = sumAllPayment.add(il.totalAmount());
        }

        assertEquals(0,sumAllPayment.compareTo(actual));
    }

    @Test
    void zeroInterest(){
        annualRatePercent = BigDecimal.ZERO;
        InstallmentScheduleMath installmentScheduleMath = new InstallmentScheduleMath();
        List<InstallmentLine> installmentLines =
                installmentScheduleMath.scheduleGenerator(principal,annualRatePercent, tenorMonths, startDate);

        BigDecimal sumAllPayment = BigDecimal.ZERO;
        for(InstallmentLine il : installmentLines){
            sumAllPayment = sumAllPayment.add(il.totalAmount());
        }
        assertEquals(0,sumAllPayment.compareTo(principal));
    }
}