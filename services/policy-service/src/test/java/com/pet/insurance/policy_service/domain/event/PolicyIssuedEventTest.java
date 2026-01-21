package com.pet.insurance.policy_service.domain.event;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PolicyIssuedEventTest {

    @Test
    void shouldCreateEventWithValidParameters() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "john@example.com";

        // When
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        assertNotNull(event);
        assertEquals(policyId, event.policyId());
        assertEquals(quotationId, event.quotationId());
        assertEquals(ownerEmail, event.ownerEmail());
    }

    @Test
    void shouldBeARecord() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "jane@example.com";

        // When
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        assertNotNull(event);
        assertTrue(event.getClass().isRecord());
    }

    @Test
    void shouldHaveImmutableFields() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "bob@example.com";

        // When
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        assertSame(policyId, event.policyId());
        assertSame(quotationId, event.quotationId());
        assertSame(ownerEmail, event.ownerEmail());
    }

    @Test
    void shouldSupportEquality() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "test@example.com";

        // When
        PolicyIssuedEvent event1 = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);
        PolicyIssuedEvent event2 = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        assertEquals(event1, event2);
    }

    @Test
    void shouldNotBeEqualWithDifferentPolicyId() {
        // Given
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "test@example.com";

        // When
        PolicyIssuedEvent event1 = new PolicyIssuedEvent(UUID.randomUUID(), quotationId, ownerEmail);
        PolicyIssuedEvent event2 = new PolicyIssuedEvent(UUID.randomUUID(), quotationId, ownerEmail);

        // Then
        assertNotEquals(event1, event2);
    }

    @Test
    void shouldNotBeEqualWithDifferentQuotationId() {
        // Given
        UUID policyId = UUID.randomUUID();
        String ownerEmail = "test@example.com";

        // When
        PolicyIssuedEvent event1 = new PolicyIssuedEvent(policyId, UUID.randomUUID(), ownerEmail);
        PolicyIssuedEvent event2 = new PolicyIssuedEvent(policyId, UUID.randomUUID(), ownerEmail);

        // Then
        assertNotEquals(event1, event2);
    }

    @Test
    void shouldNotBeEqualWithDifferentOwnerEmail() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();

        // When
        PolicyIssuedEvent event1 = new PolicyIssuedEvent(policyId, quotationId, "email1@example.com");
        PolicyIssuedEvent event2 = new PolicyIssuedEvent(policyId, quotationId, "email2@example.com");

        // Then
        assertNotEquals(event1, event2);
    }

    @Test
    void shouldHaveConsistentHashCode() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "test@example.com";

        // When
        PolicyIssuedEvent event1 = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);
        PolicyIssuedEvent event2 = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldHaveToStringImplementation() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "test@example.com";

        // When
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        String toString = event.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("PolicyIssuedEvent"));
        assertTrue(toString.contains(policyId.toString()));
        assertTrue(toString.contains(quotationId.toString()));
        assertTrue(toString.contains(ownerEmail));
    }

    @Test
    void shouldHandleEmptyEmail() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "";

        // When
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // Then
        assertEquals("", event.ownerEmail());
    }

    @Test
    void shouldAllowNullEmail() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();

        // When
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, null);

        // Then
        assertNull(event.ownerEmail());
    }
}
