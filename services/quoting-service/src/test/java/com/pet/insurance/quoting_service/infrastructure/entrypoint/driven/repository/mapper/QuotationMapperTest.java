package com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mapper;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.entity.QuotationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class QuotationMapperTest {

    private QuotationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new QuotationMapper();
    }

    @Test
    @DisplayName("Should map Quotation domain to QuotationEntity successfully")
    void shouldMapDomainToEntity() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "123",
                "Max",
                "Dog",
                "Labrador",
                5,
                true,
                new BigDecimal("150.00"),
                LocalDate.of(2026, 2, 20));

        // When
        QuotationEntity entity = mapper.toEntity(quotation);

        // Then
        assertNotNull(entity);
        assertEquals("123", entity.getId());
        assertEquals("Max", entity.getPetName());
        assertEquals("Dog", entity.getSpecies());
        assertEquals("Labrador", entity.getBreed());
        assertEquals(5, entity.getAge());
        assertTrue(entity.isPremiumPlan());
        assertEquals(new BigDecimal("150.00"), entity.getPrice());
        assertEquals(LocalDate.of(2026, 2, 20), entity.getExpiresAt());
    }

    @Test
    @DisplayName("Should map QuotationEntity to Quotation domain successfully")
    void shouldMapEntityToDomain() {
        // Given
        QuotationEntity entity = new QuotationEntity(
                "456",
                "Luna",
                "Cat",
                "Persian",
                3,
                false,
                new BigDecimal("120.00"),
                LocalDate.of(2026, 3, 15));

        // When
        Quotation quotation = mapper.toDomain(entity);

        // Then
        assertNotNull(quotation);
        assertEquals("456", quotation.id());
        assertEquals("Luna", quotation.petName());
        assertEquals("Cat", quotation.species());
        assertEquals("Persian", quotation.breed());
        assertEquals(3, quotation.age());
        assertFalse(quotation.premiumPlan());
        assertEquals(new BigDecimal("120.00"), quotation.price());
        assertEquals(LocalDate.of(2026, 3, 15), quotation.expiresAt());
    }

    @Test
    @DisplayName("Should handle null id when mapping to entity")
    void shouldHandleNullIdWhenMappingToEntity() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                null,
                "Buddy",
                "Dog",
                "Golden Retriever",
                2,
                true,
                new BigDecimal("180.00"),
                LocalDate.of(2026, 4, 10));

        // When
        QuotationEntity entity = mapper.toEntity(quotation);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Buddy", entity.getPetName());
        assertEquals("Dog", entity.getSpecies());
    }

    @Test
    @DisplayName("Should handle bidirectional mapping correctly")
    void shouldHandleBidirectionalMapping() {
        // Given
        Quotation originalQuotation = Quotation.reconstruct(
                "789",
                "Charlie",
                "Dog",
                "Beagle",
                4,
                false,
                new BigDecimal("130.00"),
                LocalDate.of(2026, 5, 20));

        // When
        QuotationEntity entity = mapper.toEntity(originalQuotation);
        Quotation mappedBackQuotation = mapper.toDomain(entity);

        // Then
        assertNotNull(mappedBackQuotation);
        assertEquals(originalQuotation.id(), mappedBackQuotation.id());
        assertEquals(originalQuotation.petName(), mappedBackQuotation.petName());
        assertEquals(originalQuotation.species(), mappedBackQuotation.species());
        assertEquals(originalQuotation.breed(), mappedBackQuotation.breed());
        assertEquals(originalQuotation.age(), mappedBackQuotation.age());
        assertEquals(originalQuotation.premiumPlan(), mappedBackQuotation.premiumPlan());
        assertEquals(originalQuotation.price(), mappedBackQuotation.price());
        assertEquals(originalQuotation.expiresAt(), mappedBackQuotation.expiresAt());
    }

    @Test
    @DisplayName("Should map premium plan boolean correctly from domain to entity")
    void shouldMapPremiumPlanBooleanCorrectly() {
        // Given - premium plan true
        Quotation quotationWithPremium = Quotation.reconstruct(
                "001",
                "Rocky",
                "Dog",
                "Rottweiler",
                6,
                true,
                new BigDecimal("200.00"),
                LocalDate.of(2026, 6, 15));

        // Given - premium plan false
        Quotation quotationWithoutPremium = Quotation.reconstruct(
                "002",
                "Milo",
                "Cat",
                "Siamese",
                2,
                false,
                new BigDecimal("100.00"),
                LocalDate.of(2026, 6, 15));

        // When
        QuotationEntity entityWithPremium = mapper.toEntity(quotationWithPremium);
        QuotationEntity entityWithoutPremium = mapper.toEntity(quotationWithoutPremium);

        // Then
        assertTrue(entityWithPremium.isPremiumPlan());
        assertFalse(entityWithoutPremium.isPremiumPlan());
    }
}
