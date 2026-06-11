package com.firstclub.membership.api.dto;

import com.firstclub.membership.domain.SubscriptionStatus;
import java.time.LocalDate;

public record SubscriptionResponse(
        Long id,
        Long userId,
        PlanResponse plan,
        TierResponse tier,
        SubscriptionStatus status,
        LocalDate startsAt,
        LocalDate expiresAt,
        Long version
) {
}
