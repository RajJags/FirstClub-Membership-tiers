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

@Entity
@Table(name = "tier_benefits")
public class TierBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tier_id", nullable = false)
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitType type;

    @Column(name = "benefit_value", nullable = false)
    private String value;

    @Column(nullable = false)
    private String description;

    protected TierBenefit() {
    }

    public TierBenefit(MembershipTier tier, BenefitType type, String value, String description) {
        this.tier = tier;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    public BenefitType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
