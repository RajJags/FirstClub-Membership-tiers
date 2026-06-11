package com.firstclub.membership.api.dto;

import com.firstclub.membership.domain.BillingPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubscribeRequest(
        @NotNull @Positive Long userId,
        @NotNull BillingPeriod billingPeriod,
        @NotBlank String tierCode
) {
}
