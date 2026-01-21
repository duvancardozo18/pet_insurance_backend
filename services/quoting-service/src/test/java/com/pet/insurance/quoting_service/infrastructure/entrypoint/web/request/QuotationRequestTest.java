package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuotationRequest Unit Tests")
class QuotationRequestTest {

    @Test
    @DisplayName("Should create request with all fields")
    void shouldCreateRequestWithAllFields() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);

        // Then
        assertThat(request).isNotNull();
        assertThat(request.name()).isEqualTo("Max");
        assertThat(request.species()).isEqualTo("Dog");
        assertThat(request.breed()).isEqualTo("Labrador");
        assertThat(request.age()).isEqualTo(5);
        assertThat(request.premium()).isTrue();
    }

    @Test
    @DisplayName("Should create request with premium plan as false")
    void shouldCreateRequestWithPremiumPlanAsFalse() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "Luna",
                "Cat",
                "Persian",
                3,
                false);

        // Then
        assertThat(request.premium()).isFalse();
        assertThat(request.name()).isEqualTo("Luna");
        assertThat(request.species()).isEqualTo("Cat");
    }

    @Test
    @DisplayName("Should create request with zero age")
    void shouldCreateRequestWithZeroAge() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "Puppy",
                "Dog",
                "Beagle",
                0,
                true);

        // Then
        assertThat(request.age()).isZero();
        assertThat(request.name()).isEqualTo("Puppy");
    }

    @Test
    @DisplayName("Should create request with null breed")
    void shouldCreateRequestWithNullBreed() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "Charlie",
                "Bird",
                null,
                2,
                false);

        // Then
        assertThat(request.breed()).isNull();
        assertThat(request.species()).isEqualTo("Bird");
    }

    @Test
    @DisplayName("Should create request with maximum age")
    void shouldCreateRequestWithMaximumAge() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "Senior",
                "Cat",
                "Maine Coon",
                10,
                true);

        // Then
        assertThat(request.age()).isEqualTo(10);
        assertThat(request.name()).isEqualTo("Senior");
    }

    @Test
    @DisplayName("Should handle records equality correctly")
    void shouldHandleRecordsEqualityCorrectly() {
        // Given
        QuotationRequest request1 = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);

        QuotationRequest request2 = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);

        // When & Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Should handle records inequality correctly")
    void shouldHandleRecordsInequalityCorrectly() {
        // Given
        QuotationRequest request1 = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);

        QuotationRequest request2 = new QuotationRequest(
                "Luna",
                "Cat",
                "Persian",
                3,
                false);

        // When & Then
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    @DisplayName("Should handle toString method")
    void shouldHandleToStringMethod() {
        // Given
        QuotationRequest request = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("Max");
        assertThat(toString).contains("Dog");
        assertThat(toString).contains("Labrador");
        assertThat(toString).contains("5");
        assertThat(toString).contains("true");
    }

    @Test
    @DisplayName("Should create request with different species")
    void shouldCreateRequestWithDifferentSpecies() {
        // Given & When
        QuotationRequest dogRequest = new QuotationRequest("Buddy", "Dog", "Poodle", 4, true);
        QuotationRequest catRequest = new QuotationRequest("Whiskers", "Cat", "Siamese", 6, false);
        QuotationRequest birdRequest = new QuotationRequest("Tweety", "Bird", "Canary", 1, false);

        // Then
        assertThat(dogRequest.species()).isEqualTo("Dog");
        assertThat(catRequest.species()).isEqualTo("Cat");
        assertThat(birdRequest.species()).isEqualTo("Bird");
    }

    @Test
    @DisplayName("Should create request with empty name")
    void shouldCreateRequestWithEmptyName() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "",
                "Dog",
                "Bulldog",
                7,
                false);

        // Then
        assertThat(request.name()).isEmpty();
        assertThat(request.species()).isEqualTo("Dog");
    }

    @Test
    @DisplayName("Should create request with long pet name")
    void shouldCreateRequestWithLongPetName() {
        // Given
        String longName = "SuperLongPetNameThatIsVeryDetailedAndDescriptive";

        // When
        QuotationRequest request = new QuotationRequest(
                longName,
                "Dog",
                "Golden Retriever",
                5,
                true);

        // Then
        assertThat(request.name()).isEqualTo(longName);
        assertThat(request.name().length()).isGreaterThan(20);
    }

    @Test
    @DisplayName("Should preserve immutability of record")
    void shouldPreserveImmutabilityOfRecord() {
        // Given
        QuotationRequest request = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);

        // When - trying to access fields multiple times
        String name1 = request.name();
        String name2 = request.name();
        int age1 = request.age();
        int age2 = request.age();

        // Then - should return the same values
        assertThat(name1).isEqualTo(name2);
        assertThat(age1).isEqualTo(age2);
    }

    @Test
    @DisplayName("Should handle special characters in names")
    void shouldHandleSpecialCharactersInNames() {
        // Given & When
        QuotationRequest request = new QuotationRequest(
                "Max-O'Connor",
                "Dog",
                "Mixed-Breed",
                3,
                true);

        // Then
        assertThat(request.name()).isEqualTo("Max-O'Connor");
        assertThat(request.breed()).isEqualTo("Mixed-Breed");
    }
}
