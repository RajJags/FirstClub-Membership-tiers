package com.firstclub.membership.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public record TierEvaluationRequest(
        @NotNull @Positive Long userId,
        @Min(0) int monthlyOrderCount,
        @NotNull @DecimalMin("0.0") BigDecimal monthlyOrderValue,
        Set<String> cohorts
) {
}
