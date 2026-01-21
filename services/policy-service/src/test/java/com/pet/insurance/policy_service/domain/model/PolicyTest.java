package com.pet.insurance.policy_service.domain.model;

import com.pet.insurance.policy_service.domain.event.PolicyIssuedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PolicyTest {

    @Test
    void shouldIssuePolicyWithValidParameters() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertNotNull(policy);
        assertNotNull(policy.getId());
        assertEquals(quotationId, policy.getQuotationId());
        assertEquals(owner, policy.getOwner());
        assertNotNull(policy.getStartDate());
        assertNotNull(policy.getEndDate());
    }

    @Test
    void shouldSetStartDateToCurrentDate() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");
        LocalDate expectedStartDate = LocalDate.now();

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertEquals(expectedStartDate, policy.getStartDate());
    }

    @Test
    void shouldSetEndDateToOneYearAfterStartDate() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");
        LocalDate expectedEndDate = LocalDate.now().plusYears(1);

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertEquals(expectedEndDate, policy.getEndDate());
    }

    @Test
    void shouldBeActiveWhenJustIssued() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertTrue(policy.isActive());
    }

    @Test
    void shouldNotBeExpiredWhenJustIssued() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertFalse(policy.isExpired());
    }

    @Test
    void shouldGenerateDifferentIdsForDifferentPolicies() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");

        // When
        Policy policy1 = Policy.issue(quotationId, owner);
        Policy policy2 = Policy.issue(quotationId, owner);

        // Then
        assertNotEquals(policy1.getId(), policy2.getId());
    }

    @Test
    void shouldCreatePolicyIssuedEvent() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");
        Policy policy = Policy.issue(quotationId, owner);

        // When
        PolicyIssuedEvent event = policy.toEvent();

        // Then
        assertNotNull(event);
        assertEquals(policy.getId(), event.policyId());
        assertEquals(quotationId, event.quotationId());
        assertEquals(owner.email(), event.ownerEmail());
    }

    @Test
    void shouldPreserveOwnerInformation() {
        // Given
        UUID quotationId = UUID.randomUUID();
        String ownerId = "456";
        String ownerName = "Jane Smith";
        String ownerEmail = "jane@example.com";
        Owner owner = new Owner(ownerId, ownerName, ownerEmail);

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertEquals(ownerId, policy.getOwner().id());
        assertEquals(ownerName, policy.getOwner().name());
        assertEquals(ownerEmail, policy.getOwner().email());
    }

    @Test
    void shouldHaveConsistentEventCreation() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("789", "Bob Johnson", "bob@example.com");
        Policy policy = Policy.issue(quotationId, owner);

        // When
        PolicyIssuedEvent event1 = policy.toEvent();
        PolicyIssuedEvent event2 = policy.toEvent();

        // Then
        assertEquals(event1.policyId(), event2.policyId());
        assertEquals(event1.quotationId(), event2.quotationId());
        assertEquals(event1.ownerEmail(), event2.ownerEmail());
    }

    @Test
    void shouldReturnCorrectQuotationId() {
        // Given
        UUID quotationId = UUID.randomUUID();
        Owner owner = new Owner("123", "John Doe", "john@example.com");

        // When
        Policy policy = Policy.issue(quotationId, owner);

        // Then
        assertEquals(quotationId, policy.getQuotationId());
    }
}
