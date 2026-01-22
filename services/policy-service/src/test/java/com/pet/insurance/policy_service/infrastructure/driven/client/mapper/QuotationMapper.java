package com.pet.insurance.policy_service.infrastructure.driven.client.mapper;

import com.pet.insurance.policy_service.domain.model.Quotation;
import com.pet.insurance.policy_service.infrastructure.driven.client.dto.QuotationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QuotationMapper Tests")
class QuotationMapperTest {

    private QuotationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new QuotationMapper();
    }

    @Test
    @DisplayName("should map QuotationDTO to Quotation domain correctly")
    void shouldMapQuotationDtoToQuotationDomainCorrectly() {
        // Arrange
        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Max");
        dto.setSpecies("Dog");
        dto.setBreed("Labrador");
        dto.setAge(3);
        dto.setPremiumPlan(true);
        dto.setPrice(new BigDecimal("50.00"));
        dto.setExpiresAt(LocalDate.now().plusDays(30));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertNotNull(quotation);
        assertEquals(dto.getId(), quotation.id());
        assertEquals(dto.getPetName(), quotation.petName());
        assertEquals(dto.getSpecies(), quotation.species());
        assertEquals(dto.getBreed(), quotation.breed());
        assertEquals(dto.getAge(), quotation.age());
        assertEquals(dto.isPremiumPlan(), quotation.premiumPlan());
        assertEquals(dto.getPrice(), quotation.price());
        assertEquals(dto.getExpiresAt(), quotation.expiresAt());
    }

    @Test
    @DisplayName("should handle premium plan true")
    void shouldHandlePremiumPlanTrue() {
        // Arrange
        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Bella");
        dto.setSpecies("Cat");
        dto.setBreed("Persian");
        dto.setAge(2);
        dto.setPremiumPlan(true);
        dto.setPrice(new BigDecimal("75.00"));
        dto.setExpiresAt(LocalDate.now().plusDays(15));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertTrue(quotation.premiumPlan());
    }

    @Test
    @DisplayName("should handle premium plan false")
    void shouldHandlePremiumPlanFalse() {
        // Arrange
        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Charlie");
        dto.setSpecies("Dog");
        dto.setBreed("Beagle");
        dto.setAge(4);
        dto.setPremiumPlan(false);
        dto.setPrice(new BigDecimal("30.00"));
        dto.setExpiresAt(LocalDate.now().plusDays(20));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertFalse(quotation.premiumPlan());
    }

    @Test
    @DisplayName("should map all string fields correctly")
    void shouldMapAllStringFieldsCorrectly() {
        // Arrange
        String expectedId = UUID.randomUUID().toString();
        String expectedPetName = "Rocky";
        String expectedSpecies = "Dog";
        String expectedBreed = "Golden Retriever";

        QuotationDTO dto = new QuotationDTO();
        dto.setId(expectedId);
        dto.setPetName(expectedPetName);
        dto.setSpecies(expectedSpecies);
        dto.setBreed(expectedBreed);
        dto.setAge(5);
        dto.setPremiumPlan(true);
        dto.setPrice(new BigDecimal("60.00"));
        dto.setExpiresAt(LocalDate.now().plusDays(25));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertEquals(expectedId, quotation.id());
        assertEquals(expectedPetName, quotation.petName());
        assertEquals(expectedSpecies, quotation.species());
        assertEquals(expectedBreed, quotation.breed());
    }

    @Test
    @DisplayName("should map numeric fields correctly")
    void shouldMapNumericFieldsCorrectly() {
        // Arrange
        int expectedAge = 7;
        BigDecimal expectedPrice = new BigDecimal("99.99");

        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Luna");
        dto.setSpecies("Cat");
        dto.setBreed("Siamese");
        dto.setAge(expectedAge);
        dto.setPremiumPlan(true);
        dto.setPrice(expectedPrice);
        dto.setExpiresAt(LocalDate.now().plusDays(10));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertEquals(expectedAge, quotation.age());
        assertEquals(expectedPrice, quotation.price());
    }

    @Test
    @DisplayName("should map date field correctly")
    void shouldMapDateFieldCorrectly() {
        // Arrange
        LocalDate expectedExpirationDate = LocalDate.of(2026, 2, 15);

        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Daisy");
        dto.setSpecies("Dog");
        dto.setBreed("Poodle");
        dto.setAge(1);
        dto.setPremiumPlan(false);
        dto.setPrice(new BigDecimal("40.00"));
        dto.setExpiresAt(expectedExpirationDate);

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertEquals(expectedExpirationDate, quotation.expiresAt());
    }

    @Test
    @DisplayName("should handle zero age")
    void shouldHandleZeroAge() {
        // Arrange
        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Puppy");
        dto.setSpecies("Dog");
        dto.setBreed("Mixed");
        dto.setAge(0);
        dto.setPremiumPlan(false);
        dto.setPrice(new BigDecimal("25.00"));
        dto.setExpiresAt(LocalDate.now().plusDays(30));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertEquals(0, quotation.age());
    }

    @Test
    @DisplayName("should handle zero price")
    void shouldHandleZeroPrice() {
        // Arrange
        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("FreeTest");
        dto.setSpecies("Cat");
        dto.setBreed("Domestic");
        dto.setAge(5);
        dto.setPremiumPlan(false);
        dto.setPrice(BigDecimal.ZERO);
        dto.setExpiresAt(LocalDate.now().plusDays(7));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertEquals(BigDecimal.ZERO, quotation.price());
    }

    @Test
    @DisplayName("should create valid domain object from DTO")
    void shouldCreateValidDomainObjectFromDto() {
        // Arrange
        QuotationDTO dto = new QuotationDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setPetName("Buddy");
        dto.setSpecies("Dog");
        dto.setBreed("Bulldog");
        dto.setAge(6);
        dto.setPremiumPlan(true);
        dto.setPrice(new BigDecimal("85.00"));
        dto.setExpiresAt(LocalDate.now().plusDays(45));

        // Act
        Quotation quotation = mapper.toDomain(dto);

        // Assert
        assertNotNull(quotation);
        assertInstanceOf(Quotation.class, quotation);
    }

    @Test
    @DisplayName("should map different pet species correctly")
    void shouldMapDifferentPetSpeciesCorrectly() {
        // Arrange
        String[] species = { "Dog", "Cat", "Bird", "Rabbit" };

        for (String speciesName : species) {
            QuotationDTO dto = new QuotationDTO();
            dto.setId(UUID.randomUUID().toString());
            dto.setPetName("Pet" + speciesName);
            dto.setSpecies(speciesName);
            dto.setBreed("Mixed");
            dto.setAge(3);
            dto.setPremiumPlan(false);
            dto.setPrice(new BigDecimal("50.00"));
            dto.setExpiresAt(LocalDate.now().plusDays(30));

            // Act
            Quotation quotation = mapper.toDomain(dto);

            // Assert
            assertEquals(speciesName, quotation.species());
        }
    }
}
