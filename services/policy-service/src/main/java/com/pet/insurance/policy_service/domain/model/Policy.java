package com.pet.insurance.policy_service.domain.model;

import com.pet.insurance.policy_service.domain.event.PolicyIssuedEvent;

import java.time.LocalDate;
import java.util.UUID;

public class Policy {

    private static final int POLICY_DURATION_YEARS = 1;

    private final UUID id;
    private final UUID quotationId;
    private final Owner owner;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private boolean active;

    private Policy(
            UUID id,
            UUID quotationId,
            Owner owner,
            LocalDate startDate,
            LocalDate endDate) {
        this.id = id;
        this.quotationId = quotationId;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
    }

    public static Policy issue(UUID quotationId, Owner owner) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusYears(POLICY_DURATION_YEARS);

        return new Policy(
                UUID.randomUUID(),
                quotationId,
                owner,
                start,
                end);
    }

    public boolean isActive() {
        return active && !isExpired();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    public PolicyIssuedEvent toEvent() {
        return new PolicyIssuedEvent(id, quotationId, owner.email());
    }

    public UUID getId() {
        return id;
    }

    public UUID getQuotationId() {
        return quotationId;
    }

    public Owner getOwner() {
        return owner;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
