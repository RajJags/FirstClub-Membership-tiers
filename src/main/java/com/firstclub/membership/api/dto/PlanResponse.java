package com.firstclub.membership.api.dto;

import com.firstclub.membership.domain.BillingPeriod;
import java.math.BigDecimal;

public record PlanResponse(Long id, BillingPeriod billingPeriod, BigDecimal price, String currency) {
}
