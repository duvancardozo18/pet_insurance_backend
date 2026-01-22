package com.pet.insurance.policy_service.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    @Test
    void shouldCreateOwnerWithValidParameters() {
        // Given
        String id = "123";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When
        Owner owner = new Owner(id, name, email);

        // Then
        assertNotNull(owner);
        assertEquals(id, owner.id());
        assertEquals(name, owner.name());
        assertEquals(email, owner.email());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new Owner(null, name, email);
        });
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Given
        String id = "123";
        String email = "john.doe@example.com";

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new Owner(id, null, email);
        });
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        // Given
        String id = "123";
        String name = "John Doe";

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new Owner(id, name, null);
        });
    }

    @Test
    void shouldAllowEmptyStringsForId() {
        // Given
        String id = "";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When
        Owner owner = new Owner(id, name, email);

        // Then
        assertNotNull(owner);
        assertEquals("", owner.id());
    }

    @Test
    void shouldAllowEmptyStringsForName() {
        // Given
        String id = "123";
        String name = "";
        String email = "john.doe@example.com";

        // When
        Owner owner = new Owner(id, name, email);

        // Then
        assertNotNull(owner);
        assertEquals("", owner.name());
    }

    @Test
    void shouldAllowEmptyStringsForEmail() {
        // Given
        String id = "123";
        String name = "John Doe";
        String email = "";

        // When
        Owner owner = new Owner(id, name, email);

        // Then
        assertNotNull(owner);
        assertEquals("", owner.email());
    }
}
