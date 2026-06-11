package com.firstclub.membership.api;

import com.firstclub.membership.api.dto.ChangeTierRequest;
import com.firstclub.membership.api.dto.SubscribeRequest;
import com.firstclub.membership.api.dto.SubscriptionResponse;
import com.firstclub.membership.service.MembershipSubscriptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/memberships")
public class MembershipSubscriptionController {

    private final MembershipSubscriptionService subscriptionService;

    public MembershipSubscriptionController(MembershipSubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse subscribe(@Valid @RequestBody SubscribeRequest request) {
        return subscriptionService.subscribe(request);
    }

    @GetMapping("/users/{userId}/subscription")
    public SubscriptionResponse current(@PathVariable @Positive Long userId) {
        return subscriptionService.current(userId);
    }

    @PutMapping("/users/{userId}/subscription/tier")
    public SubscriptionResponse changeTier(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody ChangeTierRequest request
    ) {
        return subscriptionService.changeTier(userId, request.tierCode());
    }

    @DeleteMapping("/users/{userId}/subscription")
    public SubscriptionResponse cancel(@PathVariable @Positive Long userId) {
        return subscriptionService.cancel(userId);
    }
}
