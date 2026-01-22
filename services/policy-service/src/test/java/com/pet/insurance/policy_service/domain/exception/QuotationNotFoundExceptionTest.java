package com.pet.insurance.policy_service.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuotationNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithQuotationId() {
        // Given
        String quotationId = "Q123";

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertNotNull(exception);
        assertEquals("Quotation not found with ID: Q123", exception.getMessage());
    }

    @Test
    void shouldBeRuntimeException() {
        // Given
        String quotationId = "Q456";

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldIncludeQuotationIdInMessage() {
        // Given
        String quotationId = "Q789";

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertTrue(exception.getMessage().contains(quotationId));
    }

    @Test
    void shouldHandleEmptyQuotationId() {
        // Given
        String quotationId = "";

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertEquals("Quotation not found with ID: ", exception.getMessage());
    }

    @Test
    void shouldHandleNullQuotationId() {
        // Given
        String quotationId = null;

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertEquals("Quotation not found with ID: null", exception.getMessage());
    }

    @Test
    void shouldHandleNumericQuotationId() {
        // Given
        String quotationId = "12345";

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertEquals("Quotation not found with ID: 12345", exception.getMessage());
    }

    @Test
    void shouldHandleUuidQuotationId() {
        // Given
        String quotationId = "550e8400-e29b-41d4-a716-446655440000";

        // When
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Then
        assertEquals("Quotation not found with ID: 550e8400-e29b-41d4-a716-446655440000", exception.getMessage());
    }

    @Test
    void shouldBeThrowable() {
        // Given
        String quotationId = "Q999";

        // When & Then
        assertThrows(QuotationNotFoundException.class, () -> {
            throw new QuotationNotFoundException(quotationId);
        });
    }

    @Test
    void shouldHaveDifferentMessageThanExpiredException() {
        // Given
        String quotationId = "Q111";

        // When
        QuotationNotFoundException notFoundException = new QuotationNotFoundException(quotationId);
        QuotationExpiredException expiredException = new QuotationExpiredException(quotationId);

        // Then
        assertNotEquals(notFoundException.getMessage(), expiredException.getMessage());
        assertTrue(notFoundException.getMessage().contains("not found"));
        assertTrue(expiredException.getMessage().contains("expired"));
    }
}
