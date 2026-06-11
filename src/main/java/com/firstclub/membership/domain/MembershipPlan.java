package com.firstclub.membership.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private BillingPeriod billingPeriod;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private boolean active = true;

    protected MembershipPlan() {
    }

    public MembershipPlan(BillingPeriod billingPeriod, BigDecimal price, String currency) {
        this.billingPeriod = billingPeriod;
        this.price = price;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isActive() {
        return active;
    }
}
