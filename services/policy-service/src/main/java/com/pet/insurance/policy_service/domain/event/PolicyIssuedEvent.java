package com.pet.insurance.policy_service.domain.event;

import java.util.UUID;

public record PolicyIssuedEvent(
        UUID policyId,
        UUID quotationId,
        String ownerEmail) {
}
