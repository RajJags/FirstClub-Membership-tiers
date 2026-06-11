package com.firstclub.membership.domain;

public enum BillingPeriod {
    MONTHLY(1),
    QUARTERLY(3),
    YEARLY(12);

    private final int months;

    BillingPeriod(int months) {
        this.months = months;
    }

    public int months() {
        return months;
    }
}
