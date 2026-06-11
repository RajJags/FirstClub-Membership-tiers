package com.firstclub.membership.service;

import com.firstclub.membership.domain.CriterionType;
import com.firstclub.membership.domain.MembershipTier;
import com.firstclub.membership.domain.TierCriterion;
import com.firstclub.membership.repository.MembershipTierRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class TierEligibilityService {

    private final MembershipTierRepository tierRepository;

    public TierEligibilityService(MembershipTierRepository tierRepository) {
        this.tierRepository = tierRepository;
    }

    public MembershipTier bestEligibleTier(TierEvaluationContext context) {
        return tierRepository.findAllByOrderByRankOrderAsc().stream()
                .sorted(MembershipTier.highestFirst())
                .filter(tier -> isEligible(tier, context))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No eligible membership tier configured"));
    }

    private boolean isEligible(MembershipTier tier, TierEvaluationContext context) {
        if (tier.getCriteria().isEmpty()) {
            return true;
        }
        return tier.getCriteria().stream().allMatch(criterion -> matches(criterion, context));
    }

    private boolean matches(TierCriterion criterion, TierEvaluationContext context) {
        CriterionType type = criterion.getType();
        return switch (type) {
            case MIN_MONTHLY_ORDER_COUNT -> context.monthlyOrderCount() >= Integer.parseInt(criterion.getValue());
            case MIN_MONTHLY_ORDER_VALUE -> context.monthlyOrderValue().compareTo(new BigDecimal(criterion.getValue())) >= 0;
            case COHORT -> context.belongsTo(criterion.getValue());
        };
    }
}
