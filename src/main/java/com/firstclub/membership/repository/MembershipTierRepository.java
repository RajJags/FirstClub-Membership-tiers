package com.firstclub.membership.repository;

import com.firstclub.membership.domain.MembershipTier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipTierRepository extends JpaRepository<MembershipTier, Long> {

    List<MembershipTier> findAllByOrderByRankOrderAsc();

    Optional<MembershipTier> findByCodeIgnoreCase(String code);
}
