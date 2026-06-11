package com.firstclub.membership.api.dto;

import com.firstclub.membership.domain.CriterionType;

public record CriterionResponse(CriterionType type, String value) {
}
