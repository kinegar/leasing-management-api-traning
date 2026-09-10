package com.hadinawa.leasing_management_api.util;

import com.hadinawa.leasing_management_api.model.dto.InstallmentScheduleDto;
import com.hadinawa.leasing_management_api.model.entities.InstallmentSchedule;

import java.math.BigDecimal;
import java.util.List;

public class InstallmentScheduleMath {
    public static List<InstallmentScheduleDto> scheduleGenerator(BigDecimal principal, Double AnnualFlatRate, Integer tenor){
        /**
         * Given principal P, annual flat rate r (as a percent), and tenor n (months):
         *
         * Monthly principal portion = P / n
         * Monthly interest portion = (P × r / 100) / 12 — note this is flat: calculated once on the original principal, not a shrinking balance. It's the same every month, unlike a reducing-balance/amortized loan.
         * Monthly installment total = principal portion + interest portion
         *
         * Total interest paid over the whole contract ends up being P × (r/100) × (n/12).
         */

        return null;
    }
}
