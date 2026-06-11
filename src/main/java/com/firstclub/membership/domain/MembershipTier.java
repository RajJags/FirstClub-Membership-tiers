package com.firstclub.membership.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "membership_tiers")
public class MembershipTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private int rankOrder;

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private Set<TierBenefit> benefits = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private Set<TierCriterion> criteria = new LinkedHashSet<>();

    protected MembershipTier() {
    }

    public MembershipTier(String code, String displayName, int rankOrder) {
        this.code = code;
        this.displayName = displayName;
        this.rankOrder = rankOrder;
    }

    public void addBenefit(BenefitType type, String value, String description) {
        benefits.add(new TierBenefit(this, type, value, description));
    }

    public void addCriterion(CriterionType type, String value) {
        criteria.add(new TierCriterion(this, type, value));
    }

    public static Comparator<MembershipTier> highestFirst() {
        return Comparator.comparingInt(MembershipTier::getRankOrder).reversed();
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRankOrder() {
        return rankOrder;
    }

    public Set<TierBenefit> getBenefits() {
        return benefits;
    }

    public Set<TierCriterion> getCriteria() {
        return criteria;
    }
}
