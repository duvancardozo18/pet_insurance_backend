package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.dto;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuotationDTO Unit Tests")
class QuotationDTOTest {

    @Test
    @DisplayName("Should create DTO from domain model successfully")
    void shouldCreateDTOFromDomainModel() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "test-id-123",
                "Max",
                "Dog",
                "Labrador",
                5,
                true,
                new BigDecimal("150.00"),
                LocalDate.now().plusDays(30));

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo("test-id-123");
        assertThat(dto.petName()).isEqualTo("Max");
        assertThat(dto.species()).isEqualTo("Dog");
        assertThat(dto.breed()).isEqualTo("Labrador");
        assertThat(dto.age()).isEqualTo(5);
        assertThat(dto.premiumPlan()).isTrue();
        assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(dto.expiresAt()).isEqualTo(LocalDate.now().plusDays(30));
        assertThat(dto.expired()).isFalse();
    }

    @Test
    @DisplayName("Should map expired quotation correctly")
    void shouldMapExpiredQuotationCorrectly() {
        // Given
        LocalDate expiredDate = LocalDate.now().minusDays(1);
        Quotation expiredQuotation = Quotation.reconstruct(
                "expired-id",
                "Buddy",
                "Cat",
                "Persian",
                3,
                false,
                new BigDecimal("100.00"),
                expiredDate);

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(expiredQuotation);

        // Then
        assertThat(dto.expired()).isTrue();
        assertThat(dto.expiresAt()).isEqualTo(expiredDate);
    }

    @Test
    @DisplayName("Should map active quotation correctly")
    void shouldMapActiveQuotationCorrectly() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(15);
        Quotation activeQuotation = Quotation.reconstruct(
                "active-id",
                "Luna",
                "Dog",
                "Beagle",
                2,
                true,
                new BigDecimal("200.00"),
                futureDate);

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(activeQuotation);

        // Then
        assertThat(dto.expired()).isFalse();
        assertThat(dto.expiresAt()).isEqualTo(futureDate);
    }

    @Test
    @DisplayName("Should map quotation without breed")
    void shouldMapQuotationWithoutBreed() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "no-breed-id",
                "Charlie",
                "Bird",
                null,
                1,
                false,
                new BigDecimal("50.00"),
                LocalDate.now().plusDays(30));

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto.breed()).isNull();
        assertThat(dto.petName()).isEqualTo("Charlie");
        assertThat(dto.species()).isEqualTo("Bird");
    }

    @Test
    @DisplayName("Should map quotation with zero age")
    void shouldMapQuotationWithZeroAge() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "young-pet-id",
                "Puppy",
                "Dog",
                "Poodle",
                0,
                false,
                new BigDecimal("90.00"),
                LocalDate.now().plusDays(30));

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto.age()).isZero();
        assertThat(dto.petName()).isEqualTo("Puppy");
    }

    @Test
    @DisplayName("Should map quotation with maximum age")
    void shouldMapQuotationWithMaximumAge() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "old-pet-id",
                "Senior",
                "Cat",
                "Maine Coon",
                10,
                true,
                new BigDecimal("250.00"),
                LocalDate.now().plusDays(30));

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto.age()).isEqualTo(10);
        assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("250.00"));
    }

    @Test
    @DisplayName("Should preserve all getters functionality")
    void shouldPreserveAllGettersFunctionality() {
        // Given
        LocalDate expiresAt = LocalDate.now().plusDays(20);
        Quotation quotation = Quotation.reconstruct(
                "getter-test-id",
                "TestPet",
                "TestSpecies",
                "TestBreed",
                7,
                true,
                new BigDecimal("175.50"),
                expiresAt);

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then - Test all getters
        assertThat(dto.id()).isEqualTo("getter-test-id");
        assertThat(dto.petName()).isEqualTo("TestPet");
        assertThat(dto.species()).isEqualTo("TestSpecies");
        assertThat(dto.breed()).isEqualTo("TestBreed");
        assertThat(dto.age()).isEqualTo(7);
        assertThat(dto.premiumPlan()).isTrue();
        assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("175.50"));
        assertThat(dto.expiresAt()).isEqualTo(expiresAt);
        assertThat(dto.expired()).isFalse();
    }

    @Test
    @DisplayName("Should map quotation with non-premium plan")
    void shouldMapQuotationWithNonPremiumPlan() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "non-premium-id",
                "Basic",
                "Dog",
                "Mixed",
                4,
                false,
                new BigDecimal("75.00"),
                LocalDate.now().plusDays(30));

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto.premiumPlan()).isFalse();
        assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("75.00"));
    }

    @Test
    @DisplayName("Should map quotation expiring today")
    void shouldMapQuotationExpiringToday() {
        // Given
        LocalDate today = LocalDate.now();
        Quotation quotation = Quotation.reconstruct(
                "expiring-today-id",
                "Today",
                "Cat",
                "Tabby",
                5,
                true,
                new BigDecimal("125.00"),
                today);

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto.expiresAt()).isEqualTo(today);
        assertThat(dto.expired()).isFalse(); // Today is not expired yet
    }

    @Test
    @DisplayName("Should handle decimal prices correctly")
    void shouldHandleDecimalPricesCorrectly() {
        // Given
        Quotation quotation = Quotation.reconstruct(
                "decimal-price-id",
                "Decimal",
                "Dog",
                "Terrier",
                3,
                true,
                new BigDecimal("99.99"),
                LocalDate.now().plusDays(30));

        // When
        QuotationDTO dto = QuotationDTO.fromDomain(quotation);

        // Then
        assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(dto.price().toString()).isEqualTo("99.99");
    }
}
