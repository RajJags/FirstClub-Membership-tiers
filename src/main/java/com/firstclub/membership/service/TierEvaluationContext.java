package com.firstclub.membership.service;

import java.math.BigDecimal;
import java.util.Set;

public record TierEvaluationContext(int monthlyOrderCount, BigDecimal monthlyOrderValue, Set<String> cohorts) {

    public boolean belongsTo(String cohort) {
        return cohorts != null && cohorts.stream().anyMatch(value -> value.equalsIgnoreCase(cohort));
    }
}
