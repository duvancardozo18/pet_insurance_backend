package com.pet.insurance.policy_service.infrastructure.web.response;

public record IssuePolicyResponse(
        String policyId,
        String quotationId,
        boolean active
) {
}
