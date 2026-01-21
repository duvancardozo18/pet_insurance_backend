package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.controller;

import com.pet.insurance.quoting_service.application.usecase.GenerateQuotationUseCase;
import com.pet.insurance.quoting_service.application.usecase.GetAllQuotationsUseCase;
import com.pet.insurance.quoting_service.application.usecase.GetQuotationByIdUseCase;
import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.web.dto.QuotationDTO;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.web.request.QuotationRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuotationController Unit Tests")
class QuotationControllerTest {

    @Mock
    private GenerateQuotationUseCase generateQuotationUseCase;

    @Mock
    private GetQuotationByIdUseCase getQuotationByIdUseCase;

    @Mock
    private GetAllQuotationsUseCase getAllQuotationsUseCase;

    @InjectMocks
    private QuotationController quotationController;

    private Quotation quotation;
    private QuotationRequest quotationRequest;

    @BeforeEach
    void setUp() {
        quotation = Quotation.reconstruct(
                "test-id-123",
                "Max",
                "Dog",
                "Labrador",
                5,
                true,
                new BigDecimal("150.00"),
                LocalDate.now().plusDays(30));

        quotationRequest = new QuotationRequest(
                "Max",
                "Dog",
                "Labrador",
                5,
                true);
    }

    @Test
    @DisplayName("Should generate a new quotation successfully")
    void shouldGenerateNewQuotation() {
        // Given
        when(generateQuotationUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyBoolean())).thenReturn(Mono.just(quotation));

        // When
        Mono<QuotationDTO> result = quotationController.generate(quotationRequest);

        // Then
        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertThat(dto).isNotNull();
                    assertThat(dto.id()).isEqualTo("test-id-123");
                    assertThat(dto.petName()).isEqualTo("Max");
                    assertThat(dto.species()).isEqualTo("Dog");
                    assertThat(dto.breed()).isEqualTo("Labrador");
                    assertThat(dto.age()).isEqualTo(5);
                    assertThat(dto.premiumPlan()).isTrue();
                    assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("150.00"));
                    assertThat(dto.expired()).isFalse();
                })
                .verifyComplete();

        verify(generateQuotationUseCase).execute("Max", "Dog", "Labrador", 5, true);
    }

    @Test
    @DisplayName("Should handle error when generating quotation fails")
    void shouldHandleErrorWhenGeneratingQuotationFails() {
        // Given
        when(generateQuotationUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyBoolean())).thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<QuotationDTO> result = quotationController.generate(quotationRequest);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();

        verify(generateQuotationUseCase).execute("Max", "Dog", "Labrador", 5, true);
    }

    @Test
    @DisplayName("Should get quotation by ID successfully")
    void shouldGetQuotationByIdSuccessfully() {
        // Given
        String quotationId = "test-id-123";
        when(getQuotationByIdUseCase.execute(quotationId))
                .thenReturn(Mono.just(quotation));

        // When
        Mono<QuotationDTO> result = quotationController.getById(quotationId);

        // Then
        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertThat(dto).isNotNull();
                    assertThat(dto.id()).isEqualTo(quotationId);
                    assertThat(dto.petName()).isEqualTo("Max");
                    assertThat(dto.species()).isEqualTo("Dog");
                })
                .verifyComplete();

        verify(getQuotationByIdUseCase).execute(quotationId);
    }

    @Test
    @DisplayName("Should handle error when quotation not found by ID")
    void shouldHandleErrorWhenQuotationNotFoundById() {
        // Given
        String quotationId = "non-existent-id";
        when(getQuotationByIdUseCase.execute(quotationId))
                .thenReturn(Mono.empty());

        // When
        Mono<QuotationDTO> result = quotationController.getById(quotationId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(getQuotationByIdUseCase).execute(quotationId);
    }

    @Test
    @DisplayName("Should get all quotations successfully")
    void shouldGetAllQuotationsSuccessfully() {
        // Given
        Quotation quotation2 = Quotation.reconstruct(
                "test-id-456",
                "Luna",
                "Cat",
                "Persian",
                3,
                false,
                new BigDecimal("100.00"),
                LocalDate.now().plusDays(30));

        when(getAllQuotationsUseCase.execute())
                .thenReturn(Flux.just(quotation, quotation2));

        // When
        Flux<QuotationDTO> result = quotationController.getAll();

        // Then
        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertThat(dto.id()).isEqualTo("test-id-123");
                    assertThat(dto.petName()).isEqualTo("Max");
                })
                .assertNext(dto -> {
                    assertThat(dto.id()).isEqualTo("test-id-456");
                    assertThat(dto.petName()).isEqualTo("Luna");
                    assertThat(dto.species()).isEqualTo("Cat");
                })
                .verifyComplete();

        verify(getAllQuotationsUseCase).execute();
    }

    @Test
    @DisplayName("Should return empty flux when no quotations exist")
    void shouldReturnEmptyFluxWhenNoQuotationsExist() {
        // Given
        when(getAllQuotationsUseCase.execute())
                .thenReturn(Flux.empty());

        // When
        Flux<QuotationDTO> result = quotationController.getAll();

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(getAllQuotationsUseCase).execute();
    }

    @Test
    @DisplayName("Should handle error when getting all quotations fails")
    void shouldHandleErrorWhenGettingAllQuotationsFails() {
        // Given
        when(getAllQuotationsUseCase.execute())
                .thenReturn(Flux.error(new RuntimeException("Connection error")));

        // When
        Flux<QuotationDTO> result = quotationController.getAll();

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Connection error"))
                .verify();

        verify(getAllQuotationsUseCase).execute();
    }

    @Test
    @DisplayName("Should map quotation with expired date correctly")
    void shouldMapQuotationWithExpiredDateCorrectly() {
        // Given
        Quotation expiredQuotation = Quotation.reconstruct(
                "expired-id",
                "Buddy",
                "Dog",
                "Beagle",
                4,
                false,
                new BigDecimal("120.00"),
                LocalDate.now().minusDays(1) // Expired yesterday
        );

        when(getQuotationByIdUseCase.execute("expired-id"))
                .thenReturn(Mono.just(expiredQuotation));

        // When
        Mono<QuotationDTO> result = quotationController.getById("expired-id");

        // Then
        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertThat(dto.expired()).isTrue();
                    assertThat(dto.expiresAt()).isBefore(LocalDate.now());
                })
                .verifyComplete();

        verify(getQuotationByIdUseCase).execute("expired-id");
    }

    @Test
    @DisplayName("Should generate quotation without premium plan")
    void shouldGenerateQuotationWithoutPremiumPlan() {
        // Given
        QuotationRequest nonPremiumRequest = new QuotationRequest(
                "Milo",
                "Cat",
                "Siamese",
                2,
                false);

        Quotation nonPremiumQuotation = Quotation.reconstruct(
                "test-id-789",
                "Milo",
                "Cat",
                "Siamese",
                2,
                false,
                new BigDecimal("80.00"),
                LocalDate.now().plusDays(30));

        when(generateQuotationUseCase.execute(
                "Milo",
                "Cat",
                "Siamese",
                2,
                false)).thenReturn(Mono.just(nonPremiumQuotation));

        // When
        Mono<QuotationDTO> result = quotationController.generate(nonPremiumRequest);

        // Then
        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertThat(dto.petName()).isEqualTo("Milo");
                    assertThat(dto.premiumPlan()).isFalse();
                    assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("80.00"));
                })
                .verifyComplete();

        verify(generateQuotationUseCase).execute("Milo", "Cat", "Siamese", 2, false);
    }
}
