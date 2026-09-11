package com.hadinawa.leasing_management_api.util;

import com.hadinawa.leasing_management_api.model.record.InstallmentLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstallmentScheduleMath {
    /**
     * Given principal P, annual flat rate r (as a percent), and tenor n (months):
     *
     * Monthly principal portion = P / n
     * Monthly interest portion = (P × r / 100) / 12 — note this is flat: calculated once on the original principal, not a shrinking balance. It's the same every month, unlike a reducing-balance/amortized loan.
     * Monthly installment total = principal portion + interest portion
     *
     * Total interest paid over the whole contract ends up being P × (r/100) × (n/12).
     */
    public List<InstallmentLine> scheduleGenerator(BigDecimal principal, BigDecimal annualFlatRate, int tenorMonths, LocalDate startDate){
        //Compute monthlyPrincipal = principal / tenorMonths, rounded to 2 decimal places
        // — pick a RoundingMode deliberately (HALF_UP is the usual default for money).
        BigDecimal monthlyPrincipal = principal.divide(BigDecimal.valueOf(tenorMonths),2, RoundingMode.HALF_UP);

        //Compute monthlyInterest = (principal * annualRatePercent / 100) / 12, rounded the same way.
        BigDecimal monthlyInterest = principal
                .multiply(annualFlatRate)
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP); // /100 and /12 combined

        //Loop i from 1 to tenorMonths:
        //dueDate = startDate.plusMonths(i) — from the original start date, not the previous due date (remember the drift bug).
        //If i < tenorMonths: principal portion = monthlyPrincipal
        //If i == tenorMonths (the last one): principal portion = principal - (monthlyPrincipal * (tenorMonths - 1)) —
        //  this absorbs the rounding remainder so everything sums exactly.
        //Interest portion: same idea if you want interest to sum exactly too, or just use monthlyInterest flat every month
        //  (simpler, and often how flat-rate contracts actually work — interest is genuinely identical each month,
        //     only principal remainder gets special-cased).
        //totalAmount = principalPortion + interestPortion

        List<InstallmentLine> installmentLines = new ArrayList<>();
        for (int i = 1; i <=tenorMonths ; i++) {
            LocalDate dueDate = startDate.plusMonths(i);
            BigDecimal principalPortion;
            if(i < tenorMonths){
                principalPortion = monthlyPrincipal;
            }else{
                principalPortion = principal.subtract(monthlyPrincipal.multiply(BigDecimal.valueOf(tenorMonths-1)));
            }
            installmentLines.add(new InstallmentLine(i,dueDate,principalPortion,monthlyInterest,principalPortion.add(monthlyInterest)));
        }
        return installmentLines;
    }
}
