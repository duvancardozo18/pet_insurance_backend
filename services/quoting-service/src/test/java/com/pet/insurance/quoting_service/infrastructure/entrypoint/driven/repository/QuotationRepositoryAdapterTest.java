package com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.entity.QuotationEntity;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mapper.QuotationMapper;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mongodb.QuotationMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuotationRepositoryAdapterTest {

    @Mock
    private QuotationMongoRepository mongoRepository;

    @Mock
    private QuotationMapper mapper;

    @InjectMocks
    private QuotationRepositoryAdapter repositoryAdapter;

    private Quotation testQuotation;
    private QuotationEntity testEntity;

    @BeforeEach
    void setUp() {
        testQuotation = Quotation.reconstruct(
                "123",
                "Max",
                "Dog",
                "Labrador",
                5,
                true,
                new BigDecimal("150.00"),
                LocalDate.of(2026, 2, 20));

        testEntity = new QuotationEntity(
                "123",
                "Max",
                "Dog",
                "Labrador",
                5,
                true,
                new BigDecimal("150.00"),
                LocalDate.of(2026, 2, 20));
    }

    @Test
    @DisplayName("Should save quotation successfully")
    void shouldSaveQuotationSuccessfully() {
        // Given
        when(mapper.toEntity(testQuotation)).thenReturn(testEntity);
        when(mongoRepository.save(testEntity)).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testQuotation);

        // When
        Mono<Quotation> result = repositoryAdapter.save(testQuotation);

        // Then
        StepVerifier.create(result)
                .expectNext(testQuotation)
                .verifyComplete();

        verify(mapper).toEntity(testQuotation);
        verify(mongoRepository).save(testEntity);
        verify(mapper).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should find quotation by id successfully")
    void shouldFindQuotationByIdSuccessfully() {
        // Given
        when(mongoRepository.findById("123")).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testQuotation);

        // When
        Mono<Quotation> result = repositoryAdapter.findById("123");

        // Then
        StepVerifier.create(result)
                .expectNext(testQuotation)
                .verifyComplete();

        verify(mongoRepository).findById("123");
        verify(mapper).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should return empty when quotation not found by id")
    void shouldReturnEmptyWhenQuotationNotFound() {
        // Given
        when(mongoRepository.findById(anyString())).thenReturn(Mono.empty());

        // When
        Mono<Quotation> result = repositoryAdapter.findById("999");

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(mongoRepository).findById("999");
        verify(mapper, never()).toDomain(any(QuotationEntity.class));
    }

    @Test
    @DisplayName("Should handle error when mapping invalid entity in findById")
    void shouldHandleErrorWhenMappingInvalidEntityInFindById() {
        // Given
        when(mongoRepository.findById("123")).thenReturn(Mono.just(testEntity));
        when(mapper.toDomain(testEntity))
                .thenThrow(new IllegalArgumentException("Invalid quotation data"));

        // When
        Mono<Quotation> result = repositoryAdapter.findById("123");

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(mongoRepository).findById("123");
        verify(mapper).toDomain(testEntity);
    }

    @Test
    @DisplayName("Should find all quotations successfully")
    void shouldFindAllQuotationsSuccessfully() {
        // Given
        QuotationEntity entity2 = new QuotationEntity(
                "456",
                "Luna",
                "Cat",
                "Persian",
                3,
                false,
                new BigDecimal("120.00"),
                LocalDate.of(2026, 3, 15));

        Quotation quotation2 = Quotation.reconstruct(
                "456",
                "Luna",
                "Cat",
                "Persian",
                3,
                false,
                new BigDecimal("120.00"),
                LocalDate.of(2026, 3, 15));

        when(mongoRepository.findAll()).thenReturn(Flux.just(testEntity, entity2));
        when(mapper.toDomain(testEntity)).thenReturn(testQuotation);
        when(mapper.toDomain(entity2)).thenReturn(quotation2);

        // When
        Flux<Quotation> result = repositoryAdapter.findAll();

        // Then
        StepVerifier.create(result)
                .expectNext(testQuotation)
                .expectNext(quotation2)
                .verifyComplete();

        verify(mongoRepository).findAll();
        verify(mapper, times(2)).toDomain(any(QuotationEntity.class));
    }

    @Test
    @DisplayName("Should skip invalid quotations in findAll")
    void shouldSkipInvalidQuotationsInFindAll() {
        // Given
        QuotationEntity invalidEntity = new QuotationEntity(
                "invalid",
                "Invalid",
                "Unknown",
                "Unknown",
                -1,
                false,
                new BigDecimal("0"),
                LocalDate.of(2020, 1, 1));

        when(mongoRepository.findAll()).thenReturn(Flux.just(testEntity, invalidEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testQuotation);
        when(mapper.toDomain(invalidEntity))
                .thenThrow(new IllegalArgumentException("Invalid quotation data"));

        // When
        Flux<Quotation> result = repositoryAdapter.findAll();

        // Then
        StepVerifier.create(result)
                .expectNext(testQuotation)
                .verifyComplete();

        verify(mongoRepository).findAll();
        verify(mapper, times(2)).toDomain(any(QuotationEntity.class));
    }

    @Test
    @DisplayName("Should return empty flux when no quotations exist")
    void shouldReturnEmptyFluxWhenNoQuotationsExist() {
        // Given
        when(mongoRepository.findAll()).thenReturn(Flux.empty());

        // When
        Flux<Quotation> result = repositoryAdapter.findAll();

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(mongoRepository).findAll();
        verify(mapper, never()).toDomain(any(QuotationEntity.class));
    }

    @Test
    @DisplayName("Should handle error when saving quotation")
    void shouldHandleErrorWhenSavingQuotation() {
        // Given
        when(mapper.toEntity(testQuotation)).thenReturn(testEntity);
        when(mongoRepository.save(testEntity))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<Quotation> result = repositoryAdapter.save(testQuotation);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(mapper).toEntity(testQuotation);
        verify(mongoRepository).save(testEntity);
        verify(mapper, never()).toDomain(any(QuotationEntity.class));
    }
}
