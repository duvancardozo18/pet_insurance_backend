package com.pet.insurance.policy_service.infrastructure.web.controller;

import com.pet.insurance.policy_service.application.usecase.IssuePolicyUseCase;
import com.pet.insurance.policy_service.infrastructure.web.request.IssuePolicyRequest;
import com.pet.insurance.policy_service.infrastructure.web.response.IssuePolicyResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final IssuePolicyUseCase issuePolicyUseCase;

    public PolicyController(IssuePolicyUseCase issuePolicyUseCase) {
        this.issuePolicyUseCase = issuePolicyUseCase;
    }

    @PostMapping
    public Mono<IssuePolicyResponse> issuePolicy(
            @RequestBody IssuePolicyRequest request
    ) {
        return issuePolicyUseCase.execute(
                        request.quotationId(),
                        request.ownerId(),
                        request.ownerName(),
                        request.ownerEmail()
                )
                .map(policy -> new IssuePolicyResponse(
                        policy.toEvent().policyId().toString(),
                        policy.toEvent().quotationId().toString(),
                        policy.isActive()
                ));
    }
}
