package com.firstclub.membership.api.dto;

import com.firstclub.membership.domain.BenefitType;

public record BenefitResponse(BenefitType type, String value, String description) {
}
