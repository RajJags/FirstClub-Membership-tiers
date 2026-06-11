package com.firstclub.membership.api.dto;

import java.util.List;

public record CatalogResponse(List<PlanResponse> plans, List<TierResponse> tiers) {
}
