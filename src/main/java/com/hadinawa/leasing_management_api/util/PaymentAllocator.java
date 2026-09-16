package com.hadinawa.leasing_management_api.util;

import com.hadinawa.leasing_management_api.model.entities.InstallmentSchedule;
import com.hadinawa.leasing_management_api.model.entities.InstallmentStatus;

import java.math.BigDecimal;
import java.util.List;

public class PaymentAllocator {
    public BigDecimal allocation(List<InstallmentSchedule> unpaidInstallment, BigDecimal paymentAmount){
        BigDecimal remaining = paymentAmount;
        for (InstallmentSchedule installment : unpaidInstallment){
            if(remaining.compareTo(BigDecimal.ZERO)<=0){
                break;
            }
            BigDecimal amountNeeded = installment.getRemainingAmount();
            BigDecimal amountToApply = remaining.min(amountNeeded);

            installment.setAmountPaid(installment.getAmountPaid().add(amountToApply));
            remaining = remaining.subtract(amountToApply);

            if(installment.getAmountPaid().compareTo(installment.getTotalAmount()) == 0){
                installment.setStatus(InstallmentStatus.PAID);
            }else{
                installment.setStatus(InstallmentStatus.PARTIALLY_PAID);
            }
        }
        return remaining;
    }
}
