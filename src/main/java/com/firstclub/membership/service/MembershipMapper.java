package com.firstclub.membership.service;

import com.firstclub.membership.api.dto.BenefitResponse;
import com.firstclub.membership.api.dto.CriterionResponse;
import com.firstclub.membership.api.dto.PlanResponse;
import com.firstclub.membership.api.dto.SubscriptionResponse;
import com.firstclub.membership.api.dto.TierResponse;
import com.firstclub.membership.domain.MembershipPlan;
import com.firstclub.membership.domain.MembershipTier;
import com.firstclub.membership.domain.TierBenefit;
import com.firstclub.membership.domain.TierCriterion;
import com.firstclub.membership.domain.UserMembership;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {

    public PlanResponse toPlanResponse(MembershipPlan plan) {
        return new PlanResponse(plan.getId(), plan.getBillingPeriod(), plan.getPrice(), plan.getCurrency());
    }

    public TierResponse toTierResponse(MembershipTier tier) {
        return new TierResponse(
                tier.getId(),
                tier.getCode(),
                tier.getDisplayName(),
                tier.getRankOrder(),
                toBenefitResponses(tier.getBenefits()),
                toCriterionResponses(tier.getCriteria())
        );
    }

    public SubscriptionResponse toSubscriptionResponse(UserMembership membership) {
        return new SubscriptionResponse(
                membership.getId(),
                membership.getUserId(),
                toPlanResponse(membership.getPlan()),
                toTierResponse(membership.getTier()),
                membership.getStatus(),
                membership.getStartsAt(),
                membership.getExpiresAt(),
                membership.getVersion()
        );
    }

    private List<BenefitResponse> toBenefitResponses(Collection<TierBenefit> benefits) {
        return benefits.stream()
                .map(benefit -> new BenefitResponse(benefit.getType(), benefit.getValue(), benefit.getDescription()))
                .toList();
    }

    private List<CriterionResponse> toCriterionResponses(Collection<TierCriterion> criteria) {
        return criteria.stream()
                .map(criterion -> new CriterionResponse(criterion.getType(), criterion.getValue()))
                .toList();
    }
}
