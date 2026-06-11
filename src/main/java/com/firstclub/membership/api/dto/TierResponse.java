package com.firstclub.membership.api.dto;

import java.util.List;

public record TierResponse(
        Long id,
        String code,
        String displayName,
        int rankOrder,
        List<BenefitResponse> benefits,
        List<CriterionResponse> criteria
) {
}
