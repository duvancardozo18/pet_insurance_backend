package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.exception.InvalidPetAgeException;
import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateQuotationUseCaseTest {

    @Mock
    private QuotationRepository repository;

    private GenerateQuotationUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GenerateQuotationUseCase(repository);
    }

    @Test
    void shouldGenerateQuotationForYoungDog() {
        // Given
        String petName = "Max";
        String species = "DOG";
        String breed = "Golden Retriever";
        int age = 3;
        boolean premiumPlan = false;

        when(repository.save(any(Quotation.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals(petName, quotation.petName());
                    assertEquals(species, quotation.species());
                    assertEquals(breed, quotation.breed());
                    assertEquals(age, quotation.age());
                    assertFalse(quotation.premiumPlan());
                    // Price: 10 * 1.2 (DOG) = 12
                    assertEquals(new BigDecimal("12.0"), quotation.price());
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(Quotation.class));
    }

    @Test
    void shouldGenerateQuotationForYoungCat() {
        // Given
        String petName = "Luna";
        String species = "CAT";
        String breed = "Persian";
        int age = 2;
        boolean premiumPlan = false;

        when(repository.save(any(Quotation.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals(petName, quotation.petName());
                    assertEquals(species, quotation.species());
                    // Price: 10 * 1.1 (CAT) = 11
                    assertEquals(new BigDecimal("11.0"), quotation.price());
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(Quotation.class));
    }

    @Test
    void shouldGenerateQuotationForOldDog() {
        // Given
        String petName = "Old Max";
        String species = "DOG";
        String breed = "Beagle";
        int age = 8;
        boolean premiumPlan = false;

        when(repository.save(any(Quotation.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals(age, quotation.age());
                    // Price: 10 * 1.2 (DOG) * 1.5 (age > 5) = 18
                    assertEquals(new BigDecimal("18.00"), quotation.price());
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(Quotation.class));
    }

    @Test
    void shouldGenerateQuotationWithPremiumPlan() {
        // Given
        String petName = "Premium Dog";
        String species = "DOG";
        String breed = "Bulldog";
        int age = 4;
        boolean premiumPlan = true;

        when(repository.save(any(Quotation.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertTrue(quotation.premiumPlan());
                    // Price: 10 * 1.2 (DOG) * 2 (premium) = 24
                    assertEquals(new BigDecimal("24.0"), quotation.price());
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(Quotation.class));
    }

    @Test
    void shouldGenerateQuotationForOldCatWithPremium() {
        // Given
        String petName = "Expensive Cat";
        String species = "CAT";
        String breed = "Siamese";
        int age = 7;
        boolean premiumPlan = true;

        when(repository.save(any(Quotation.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    // Price: 10 * 1.1 (CAT) * 1.5 (age > 5) * 2 (premium) = 33
                    assertEquals(0, new BigDecimal("33.0").compareTo(quotation.price()));
                })
                .verifyComplete();

        verify(repository, times(1)).save(any(Quotation.class));
    }

    @Test
    void shouldFailWhenAgeExceedsMaximum() {
        // Given
        String petName = "Too Old";
        String species = "DOG";
        String breed = "Mix";
        int age = 11;
        boolean premiumPlan = false;

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .expectError(InvalidPetAgeException.class)
                .verify();

        verify(repository, never()).save(any(Quotation.class));
    }

    @Test
    void shouldFailWhenPetNameIsNull() {
        // Given
        String petName = null;
        String species = "DOG";
        String breed = "Mix";
        int age = 5;
        boolean premiumPlan = false;

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).save(any(Quotation.class));
    }

    @Test
    void shouldFailWhenPetNameIsEmpty() {
        // Given
        String petName = "   ";
        String species = "DOG";
        String breed = "Mix";
        int age = 5;
        boolean premiumPlan = false;

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).save(any(Quotation.class));
    }

    @Test
    void shouldFailWhenSpeciesIsNull() {
        // Given
        String petName = "Max";
        String species = null;
        String breed = "Mix";
        int age = 5;
        boolean premiumPlan = false;

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repository, never()).save(any(Quotation.class));
    }

    @Test
    void shouldPropagateRepositoryError() {
        // Given
        String petName = "Max";
        String species = "DOG";
        String breed = "Mix";
        int age = 5;
        boolean premiumPlan = false;
        RuntimeException repositoryError = new RuntimeException("Database error");

        when(repository.save(any(Quotation.class)))
                .thenReturn(Mono.error(repositoryError));

        // When
        Mono<Quotation> result = useCase.execute(petName, species, breed, age, premiumPlan);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldCallRepositoryWithCorrectQuotation() {
        // Given
        String petName = "Max";
        String species = "DOG";
        String breed = "Golden Retriever";
        int age = 3;
        boolean premiumPlan = false;
        ArgumentCaptor<Quotation> quotationCaptor = ArgumentCaptor.forClass(Quotation.class);

        when(repository.save(any(Quotation.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When
        useCase.execute(petName, species, breed, age, premiumPlan).block();

        // Then
        verify(repository).save(quotationCaptor.capture());
        Quotation savedQuotation = quotationCaptor.getValue();

        assertEquals(petName, savedQuotation.petName());
        assertEquals(species, savedQuotation.species());
        assertEquals(breed, savedQuotation.breed());
        assertEquals(age, savedQuotation.age());
        assertEquals(premiumPlan, savedQuotation.premiumPlan());
        assertNotNull(savedQuotation.id());
        assertNotNull(savedQuotation.expiresAt());
    }
}
