package com.pet.insurance.policy_service.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class QuotationTest {

    @Test
    void shouldReconstructQuotationWithValidParameters() {
        // Given
        String id = "Q123";
        String petName = "Max";
        String species = "Dog";
        String breed = "Labrador";
        int age = 3;
        boolean premiumPlan = true;
        BigDecimal price = new BigDecimal("49.99");
        LocalDate expiresAt = LocalDate.now().plusDays(30);

        // When
        Quotation quotation = Quotation.reconstruct(id, petName, species, breed, age, premiumPlan, price, expiresAt);

        // Then
        assertNotNull(quotation);
        assertEquals(id, quotation.id());
        assertEquals(petName, quotation.petName());
        assertEquals(species, quotation.species());
        assertEquals(breed, quotation.breed());
        assertEquals(age, quotation.age());
        assertEquals(premiumPlan, quotation.premiumPlan());
        assertEquals(price, quotation.price());
        assertEquals(expiresAt, quotation.expiresAt());
    }

    @Test
    void shouldReturnFalseWhenQuotationIsNotExpired() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(30);
        Quotation quotation = Quotation.reconstruct(
                "Q123",
                "Max",
                "Dog",
                "Labrador",
                3,
                true,
                new BigDecimal("49.99"),
                futureDate);

        // When
        boolean isExpired = quotation.isExpired();

        // Then
        assertFalse(isExpired);
    }

    @Test
    void shouldReturnTrueWhenQuotationIsExpired() {
        // Given
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Quotation quotation = Quotation.reconstruct(
                "Q123",
                "Max",
                "Dog",
                "Labrador",
                3,
                true,
                new BigDecimal("49.99"),
                pastDate);

        // When
        boolean isExpired = quotation.isExpired();

        // Then
        assertTrue(isExpired);
    }

    @Test
    void shouldReturnFalseWhenExpirationDateIsToday() {
        // Given
        LocalDate today = LocalDate.now();
        Quotation quotation = Quotation.reconstruct(
                "Q123",
                "Max",
                "Dog",
                "Labrador",
                3,
                true,
                new BigDecimal("49.99"),
                today);

        // When
        boolean isExpired = quotation.isExpired();

        // Then
        assertFalse(isExpired);
    }

    @Test
    void shouldHandleBasicPlanQuotation() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "Q456",
                "Buddy",
                "Cat",
                "Persian",
                2,
                false,
                new BigDecimal("29.99"),
                LocalDate.now().plusDays(15));

        // When & Then
        assertFalse(quotation.premiumPlan());
        assertEquals(new BigDecimal("29.99"), quotation.price());
    }

    @Test
    void shouldHandleDifferentPetSpecies() {
        // Given
        Quotation dogQuotation = Quotation.reconstruct(
                "Q101",
                "Rex",
                "Dog",
                "German Shepherd",
                5,
                true,
                new BigDecimal("59.99"),
                LocalDate.now().plusDays(20));

        Quotation catQuotation = Quotation.reconstruct(
                "Q102",
                "Whiskers",
                "Cat",
                "Siamese",
                4,
                true,
                new BigDecimal("49.99"),
                LocalDate.now().plusDays(20));

        // When & Then
        assertEquals("Dog", dogQuotation.species());
        assertEquals("Cat", catQuotation.species());
    }

    @Test
    void shouldHandleZeroAge() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "Q789",
                "Puppy",
                "Dog",
                "Beagle",
                0,
                false,
                new BigDecimal("39.99"),
                LocalDate.now().plusDays(10));

        // When & Then
        assertEquals(0, quotation.age());
    }

    @Test
    void shouldHandleOldAge() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "Q999",
                "OldDog",
                "Dog",
                "Bulldog",
                15,
                true,
                new BigDecimal("99.99"),
                LocalDate.now().plusDays(5));

        // When & Then
        assertEquals(15, quotation.age());
    }
}
