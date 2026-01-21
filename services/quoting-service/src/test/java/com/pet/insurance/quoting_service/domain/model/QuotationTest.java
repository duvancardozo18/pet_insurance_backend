package com.pet.insurance.quoting_service.domain.model;

import com.pet.insurance.quoting_service.domain.exception.InvalidPetAgeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class QuotationTest {

    @Test
    void shouldCreateValidQuotation() {
        Quotation quotation = Quotation.create(
                "Max",
                "DOG",
                "Golden Retriever",
                3,
                false,
                BigDecimal.TEN);

        assertNotNull(quotation.id());
        assertEquals("Max", quotation.petName());
        assertEquals("DOG", quotation.species());
        assertEquals("Golden Retriever", quotation.breed());
        assertEquals(3, quotation.age());
        assertFalse(quotation.premiumPlan());
        assertEquals(BigDecimal.TEN, quotation.price());
        assertFalse(quotation.isExpired());
    }

    @Test
    void shouldCreateQuotationWithPremiumPlan() {
        Quotation quotation = Quotation.create(
                "Luna",
                "CAT",
                "Persian",
                2,
                true,
                new BigDecimal("20.00"));

        assertTrue(quotation.premiumPlan());
        assertEquals(new BigDecimal("20.00"), quotation.price());
    }

    @Test
    void shouldCreateQuotationWithMaximumValidAge() {
        Quotation quotation = Quotation.create(
                "Old Max",
                "DOG",
                "Beagle",
                10,
                false,
                BigDecimal.TEN);

        assertEquals(10, quotation.age());
        assertNotNull(quotation.id());
    }

    @Test
    void shouldCreateQuotationWithMinimumValidAge() {
        Quotation quotation = Quotation.create(
                "Puppy",
                "DOG",
                "Chihuahua",
                0,
                false,
                BigDecimal.TEN);

        assertEquals(0, quotation.age());
    }

    @Test
    void shouldThrowExceptionWhenAgeExceedsMaximum() {
        InvalidPetAgeException exception = assertThrows(InvalidPetAgeException.class, () -> Quotation.create(
                "Too Old",
                "DOG",
                "Bulldog",
                11,
                false,
                BigDecimal.TEN));

        assertEquals("Pets older than 10 years cannot be insured", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { 11, 15, 20, 100 })
    void shouldThrowExceptionForAnyAgeAboveTen(int age) {
        assertThrows(InvalidPetAgeException.class, () -> Quotation.create(
                "Test Pet",
                "DOG",
                "Mix",
                age,
                false,
                BigDecimal.TEN));
    }

    @Test
    void shouldThrowExceptionWhenAgeIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                "Invalid",
                "DOG",
                "Mix",
                -1,
                false,
                BigDecimal.TEN));

        assertEquals("Pet age cannot be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPetNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                null,
                "DOG",
                "Mix",
                5,
                false,
                BigDecimal.TEN));

        assertEquals("Pet name cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPetNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                "   ",
                "DOG",
                "Mix",
                5,
                false,
                BigDecimal.TEN));

        assertEquals("Pet name cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                "Max",
                null,
                "Mix",
                5,
                false,
                BigDecimal.TEN));

        assertEquals("Species cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSpeciesIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                "Max",
                "",
                "Mix",
                5,
                false,
                BigDecimal.TEN));

        assertEquals("Species cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                "Max",
                "DOG",
                "Mix",
                5,
                false,
                null));

        assertEquals("Price cannot be null or negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Quotation.create(
                "Max",
                "DOG",
                "Mix",
                5,
                false,
                new BigDecimal("-10")));

        assertEquals("Price cannot be null or negative", exception.getMessage());
    }

    @Test
    void shouldAcceptZeroPrice() {
        Quotation quotation = Quotation.create(
                "Free Pet",
                "CAT",
                "Mix",
                3,
                false,
                BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, quotation.price());
    }

    @Test
    void shouldCreateQuotationWithExpirationDate() {
        Quotation quotation = Quotation.create(
                "Max",
                "DOG",
                "Mix",
                5,
                false,
                BigDecimal.TEN);

        assertNotNull(quotation.expiresAt());
        assertTrue(quotation.expiresAt().isAfter(LocalDate.now()));
        assertEquals(LocalDate.now().plusDays(30), quotation.expiresAt());
    }

    @Test
    void shouldNotBeExpiredWhenCreated() {
        Quotation quotation = Quotation.create(
                "Max",
                "DOG",
                "Mix",
                5,
                false,
                BigDecimal.TEN);

        assertFalse(quotation.isExpired());
    }

    @Test
    void shouldBeExpiredWhenExpirationDateIsInThePast() {
        Quotation quotation = Quotation.reconstruct(
                "test-id",
                "Max",
                "DOG",
                "Mix",
                5,
                false,
                BigDecimal.TEN,
                LocalDate.now().minusDays(1));

        assertTrue(quotation.isExpired());
    }

    @Test
    void shouldNotBeExpiredWhenExpirationDateIsToday() {
        Quotation quotation = Quotation.reconstruct(
                "test-id",
                "Max",
                "DOG",
                "Mix",
                5,
                false,
                BigDecimal.TEN,
                LocalDate.now());

        assertFalse(quotation.isExpired());
    }

    @Test
    void shouldReconstructQuotationWithAllFields() {
        String expectedId = "existing-id";
        LocalDate expectedExpiresAt = LocalDate.now().plusDays(15);

        Quotation quotation = Quotation.reconstruct(
                expectedId,
                "Bella",
                "CAT",
                "Siamese",
                4,
                true,
                new BigDecimal("30.00"),
                expectedExpiresAt);

        assertEquals(expectedId, quotation.id());
        assertEquals("Bella", quotation.petName());
        assertEquals("CAT", quotation.species());
        assertEquals("Siamese", quotation.breed());
        assertEquals(4, quotation.age());
        assertTrue(quotation.premiumPlan());
        assertEquals(new BigDecimal("30.00"), quotation.price());
        assertEquals(expectedExpiresAt, quotation.expiresAt());
    }
}