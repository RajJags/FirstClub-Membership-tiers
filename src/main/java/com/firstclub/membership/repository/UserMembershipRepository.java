package com.firstclub.membership.repository;

import com.firstclub.membership.domain.SubscriptionStatus;
import com.firstclub.membership.domain.UserMembership;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {

    Optional<UserMembership> findFirstByUserIdOrderByIdDesc(Long userId);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<UserMembership> findFirstByUserIdAndStatusOrderByIdDesc(Long userId, SubscriptionStatus status);
}
