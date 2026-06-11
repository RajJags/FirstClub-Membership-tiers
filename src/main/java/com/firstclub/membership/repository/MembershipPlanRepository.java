package com.firstclub.membership.repository;

import com.firstclub.membership.domain.BillingPeriod;
import com.firstclub.membership.domain.MembershipPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    List<MembershipPlan> findByActiveTrueOrderByIdAsc();

    Optional<MembershipPlan> findByBillingPeriodAndActiveTrue(BillingPeriod billingPeriod);
}
