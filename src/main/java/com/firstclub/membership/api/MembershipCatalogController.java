package com.firstclub.membership.api;

import com.firstclub.membership.api.dto.CatalogResponse;
import com.firstclub.membership.api.dto.TierEvaluationRequest;
import com.firstclub.membership.api.dto.TierResponse;
import com.firstclub.membership.service.MembershipCatalogService;
import com.firstclub.membership.service.MembershipMapper;
import com.firstclub.membership.service.TierEligibilityService;
import com.firstclub.membership.service.TierEvaluationContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memberships")
public class MembershipCatalogController {

    private final MembershipCatalogService catalogService;
    private final TierEligibilityService eligibilityService;
    private final MembershipMapper mapper;

    public MembershipCatalogController(
            MembershipCatalogService catalogService,
            TierEligibilityService eligibilityService,
            MembershipMapper mapper
    ) {
        this.catalogService = catalogService;
        this.eligibilityService = eligibilityService;
        this.mapper = mapper;
    }

    @GetMapping("/catalog")
    public CatalogResponse catalog() {
        return catalogService.catalog();
    }

    @PostMapping("/tiers/evaluate")
    public TierResponse evaluateTier(@Valid @RequestBody TierEvaluationRequest request) {
        TierEvaluationContext context = new TierEvaluationContext(
                request.monthlyOrderCount(),
                request.monthlyOrderValue(),
                request.cohorts()
        );
        return mapper.toTierResponse(eligibilityService.bestEligibleTier(context));
    }
}
