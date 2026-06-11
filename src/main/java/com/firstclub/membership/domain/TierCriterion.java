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
@Table(name = "tier_criteria")
public class TierCriterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tier_id", nullable = false)
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CriterionType type;

    @Column(name = "criterion_value", nullable = false)
    private String value;

    protected TierCriterion() {
    }

    public TierCriterion(MembershipTier tier, CriterionType type, String value) {
        this.tier = tier;
        this.type = type;
        this.value = value;
    }

    public CriterionType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
