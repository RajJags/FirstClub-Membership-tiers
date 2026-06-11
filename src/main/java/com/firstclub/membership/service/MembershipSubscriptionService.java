package com.firstclub.membership.service;

import com.firstclub.membership.api.dto.SubscriptionResponse;
import com.firstclub.membership.api.dto.SubscribeRequest;
import com.firstclub.membership.domain.MembershipPlan;
import com.firstclub.membership.domain.MembershipTier;
import com.firstclub.membership.domain.SubscriptionStatus;
import com.firstclub.membership.domain.UserMembership;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import com.firstclub.membership.repository.UserMembershipRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MembershipSubscriptionService {

    private final UserMembershipRepository membershipRepository;
    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final MembershipMapper mapper;
    private final Clock clock;

    public MembershipSubscriptionService(
            UserMembershipRepository membershipRepository,
            MembershipPlanRepository planRepository,
            MembershipTierRepository tierRepository,
            MembershipMapper mapper,
            Clock clock
    ) {
        this.membershipRepository = membershipRepository;
        this.planRepository = planRepository;
        this.tierRepository = tierRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Transactional
    public SubscriptionResponse subscribe(SubscribeRequest request) {
        MembershipPlan plan = planRepository.findByBillingPeriodAndActiveTrue(request.billingPeriod())
                .orElseThrow(() -> new IllegalArgumentException("Active plan not found: " + request.billingPeriod()));
        MembershipTier tier = findTier(request.tierCode());
        Optional<UserMembership> existingMembership = membershipRepository.findFirstByUserIdOrderByIdDesc(request.userId());
        UserMembership membership;
        if (existingMembership.isPresent()) {
            membership = existingMembership.get();
            membership.expireIfNeeded(LocalDate.now(clock));
            if (membership.getStatus() == SubscriptionStatus.ACTIVE) {
                throw new IllegalArgumentException("User already has an active membership");
            }
            membership.renew(plan, tier, LocalDate.now(clock));
        } else {
            membership = new UserMembership(request.userId(), plan, tier, LocalDate.now(clock));
        }
        return mapper.toSubscriptionResponse(membershipRepository.saveAndFlush(membership));
    }

    @Transactional
    public SubscriptionResponse changeTier(Long userId, String tierCode) {
        UserMembership membership = findActiveMembership(userId);
        membership.changeTier(findTier(tierCode));
        return mapper.toSubscriptionResponse(membershipRepository.saveAndFlush(membership));
    }

    @Transactional
    public SubscriptionResponse cancel(Long userId) {
        UserMembership membership = findActiveMembership(userId);
        membership.cancel();
        return mapper.toSubscriptionResponse(membershipRepository.saveAndFlush(membership));
    }

    @Transactional
    public SubscriptionResponse current(Long userId) {
        UserMembership membership = membershipRepository.findFirstByUserIdOrderByIdDesc(userId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found for user: " + userId));
        membership.expireIfNeeded(LocalDate.now(clock));
        return mapper.toSubscriptionResponse(membership);
    }

    private UserMembership findActiveMembership(Long userId) {
        return membershipRepository.findFirstByUserIdAndStatusOrderByIdDesc(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Active membership not found for user: " + userId));
    }

    private MembershipTier findTier(String tierCode) {
        return tierRepository.findByCodeIgnoreCase(tierCode)
                .orElseThrow(() -> new IllegalArgumentException("Tier not found: " + tierCode));
    }
}
