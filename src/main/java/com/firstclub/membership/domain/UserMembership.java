package com.firstclub.membership.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_memberships",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_membership_user", columnNames = "user_id")
)
public class UserMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan plan;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tier_id", nullable = false)
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private LocalDate startsAt;

    @Column(nullable = false)
    private LocalDate expiresAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    protected UserMembership() {
    }

    public UserMembership(Long userId, MembershipPlan plan, MembershipTier tier, LocalDate startsAt) {
        this.userId = userId;
        renew(plan, tier, startsAt);
    }

    public void renew(MembershipPlan plan, MembershipTier tier, LocalDate startsAt) {
        this.plan = plan;
        this.tier = tier;
        this.status = SubscriptionStatus.ACTIVE;
        this.startsAt = startsAt;
        this.expiresAt = startsAt.plusMonths(plan.getBillingPeriod().months());
        this.updatedAt = LocalDateTime.now();
    }

    public void changeTier(MembershipTier tier) {
        ensureActive();
        this.tier = tier;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        ensureActive();
        this.status = SubscriptionStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void expireIfNeeded(LocalDate today) {
        if (status == SubscriptionStatus.ACTIVE && expiresAt.isBefore(today)) {
            status = SubscriptionStatus.EXPIRED;
            updatedAt = LocalDateTime.now();
        }
    }

    private void ensureActive() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active memberships can be changed");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public MembershipPlan getPlan() {
        return plan;
    }

    public MembershipTier getTier() {
        return tier;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public LocalDate getStartsAt() {
        return startsAt;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public Long getVersion() {
        return version;
    }
}
