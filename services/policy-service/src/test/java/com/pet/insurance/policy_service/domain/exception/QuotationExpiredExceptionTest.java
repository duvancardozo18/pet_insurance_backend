package com.pet.insurance.policy_service.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuotationExpiredExceptionTest {

    @Test
    void shouldCreateExceptionWithQuotationId() {
        // Given
        String quotationId = "Q123";

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertNotNull(exception);
        assertEquals("Quotation has expired with ID: Q123", exception.getMessage());
    }

    @Test
    void shouldBeRuntimeException() {
        // Given
        String quotationId = "Q456";

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldIncludeQuotationIdInMessage() {
        // Given
        String quotationId = "Q789";

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertTrue(exception.getMessage().contains(quotationId));
    }

    @Test
    void shouldHandleEmptyQuotationId() {
        // Given
        String quotationId = "";

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertEquals("Quotation has expired with ID: ", exception.getMessage());
    }

    @Test
    void shouldHandleNullQuotationId() {
        // Given
        String quotationId = null;

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertEquals("Quotation has expired with ID: null", exception.getMessage());
    }

    @Test
    void shouldHandleNumericQuotationId() {
        // Given
        String quotationId = "12345";

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertEquals("Quotation has expired with ID: 12345", exception.getMessage());
    }

    @Test
    void shouldHandleUuidQuotationId() {
        // Given
        String quotationId = "550e8400-e29b-41d4-a716-446655440000";

        // When
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Then
        assertEquals("Quotation has expired with ID: 550e8400-e29b-41d4-a716-446655440000", exception.getMessage());
    }

    @Test
    void shouldBeThrowable() {
        // Given
        String quotationId = "Q999";

        // When & Then
        assertThrows(QuotationExpiredException.class, () -> {
            throw new QuotationExpiredException(quotationId);
        });
    }
}
