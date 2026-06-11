package com.firstclub.membership.config;

import com.firstclub.membership.domain.BenefitType;
import com.firstclub.membership.domain.BillingPeriod;
import com.firstclub.membership.domain.CriterionType;
import com.firstclub.membership.domain.MembershipPlan;
import com.firstclub.membership.domain.MembershipTier;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDataSeeder {

    @Bean
    CommandLineRunner seedMembershipData(
            MembershipPlanRepository planRepository,
            MembershipTierRepository tierRepository
    ) {
        return args -> {
            if (planRepository.count() == 0) {
                planRepository.save(new MembershipPlan(BillingPeriod.MONTHLY, new BigDecimal("199.00"), "INR"));
                planRepository.save(new MembershipPlan(BillingPeriod.QUARTERLY, new BigDecimal("499.00"), "INR"));
                planRepository.save(new MembershipPlan(BillingPeriod.YEARLY, new BigDecimal("1499.00"), "INR"));
            }

            if (tierRepository.count() == 0) {
                MembershipTier silver = new MembershipTier("SILVER", "Silver", 1);
                silver.addBenefit(BenefitType.FREE_DELIVERY, "orders_above_499", "Free delivery on eligible orders");
                silver.addBenefit(BenefitType.EXTRA_DISCOUNT, "5", "Extra 5% discount on selected categories");

                MembershipTier gold = new MembershipTier("GOLD", "Gold", 2);
                gold.addCriterion(CriterionType.MIN_MONTHLY_ORDER_COUNT, "5");
                gold.addCriterion(CriterionType.MIN_MONTHLY_ORDER_VALUE, "5000");
                gold.addBenefit(BenefitType.FREE_DELIVERY, "all_eligible_orders", "Free delivery on eligible orders");
                gold.addBenefit(BenefitType.EXTRA_DISCOUNT, "10", "Extra 10% discount on selected categories");
                gold.addBenefit(BenefitType.EARLY_SALE_ACCESS, "true", "Early access to sales");

                MembershipTier platinum = new MembershipTier("PLATINUM", "Platinum", 3);
                platinum.addCriterion(CriterionType.MIN_MONTHLY_ORDER_COUNT, "10");
                platinum.addCriterion(CriterionType.MIN_MONTHLY_ORDER_VALUE, "15000");
                platinum.addBenefit(BenefitType.FREE_DELIVERY, "all_orders", "Free delivery on eligible orders");
                platinum.addBenefit(BenefitType.EXTRA_DISCOUNT, "15", "Extra 15% discount on selected categories");
                platinum.addBenefit(BenefitType.EXCLUSIVE_DEALS, "true", "Access to exclusive deals");
                platinum.addBenefit(BenefitType.PRIORITY_SUPPORT, "true", "Priority support");
                platinum.addBenefit(BenefitType.FASTER_DELIVERY, "true", "Faster delivery where available");

                tierRepository.save(silver);
                tierRepository.save(gold);
                tierRepository.save(platinum);
            }
        };
    }
}
