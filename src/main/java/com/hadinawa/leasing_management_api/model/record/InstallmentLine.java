package com.hadinawa.leasing_management_api.model.record;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentLine(int number, LocalDate dueDate, BigDecimal principalPortion, BigDecimal interestPortion, BigDecimal totalAmount) {}
