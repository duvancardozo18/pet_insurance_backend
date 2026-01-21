package com.pet.insurance.policy_service.infrastructure.web.request;

public record IssuePolicyRequest(
        String quotationId,
        String ownerId,
        String ownerName,
        String ownerEmail
) {
}
