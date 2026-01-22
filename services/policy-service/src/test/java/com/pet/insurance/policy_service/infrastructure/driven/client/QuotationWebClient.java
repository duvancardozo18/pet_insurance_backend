package com.pet.insurance.policy_service.infrastructure.driven.client;

import com.pet.insurance.policy_service.domain.exception.QuotationNotFoundException;
import com.pet.insurance.policy_service.domain.model.Quotation;
import com.pet.insurance.policy_service.infrastructure.driven.client.dto.QuotationDTO;
import com.pet.insurance.policy_service.infrastructure.driven.client.mapper.QuotationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuotationWebClient Tests")
class QuotationWebClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private QuotationMapper mapper;
    private QuotationWebClient quotationWebClient;
    private String quotingServiceUrl;
    private QuotationDTO testQuotationDTO;

    @BeforeEach
    void setUp() {
        quotingServiceUrl = "http://localhost:8081/quotations";
        mapper = new QuotationMapper();

        testQuotationDTO = new QuotationDTO();
        testQuotationDTO.setId(UUID.randomUUID().toString());
        testQuotationDTO.setPetName("Max");
        testQuotationDTO.setSpecies("Dog");
        testQuotationDTO.setBreed("Labrador");
        testQuotationDTO.setAge(3);
        testQuotationDTO.setPremiumPlan(true);
        testQuotationDTO.setPrice(new BigDecimal("50.00"));
        testQuotationDTO.setExpiresAt(LocalDate.now().plusDays(30));

        when(webClientBuilder.baseUrl(quotingServiceUrl)).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        quotationWebClient = new QuotationWebClient(webClientBuilder, mapper, quotingServiceUrl);
    }

    @Test
    @DisplayName("should find quotation by id successfully")
    void shouldFindQuotationByIdSuccessfully() {
        // Arrange
        String quotationId = testQuotationDTO.getId();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(QuotationDTO.class)).thenReturn(Mono.just(testQuotationDTO));

        // Act & Assert
        StepVerifier.create(quotationWebClient.findById(quotationId))
                .assertNext(quotation -> {
                    assertNotNull(quotation);
                    assertEquals(testQuotationDTO.getId(), quotation.id());
                    assertEquals(testQuotationDTO.getPetName(), quotation.petName());
                    assertEquals(testQuotationDTO.getSpecies(), quotation.species());
                    assertEquals(testQuotationDTO.getBreed(), quotation.breed());
                    assertEquals(testQuotationDTO.getAge(), quotation.age());
                    assertEquals(testQuotationDTO.isPremiumPlan(), quotation.premiumPlan());
                    assertEquals(testQuotationDTO.getPrice(), quotation.price());
                    assertEquals(testQuotationDTO.getExpiresAt(), quotation.expiresAt());
                })
                .verifyComplete();

        verify(webClient, times(1)).get();
    }

    @Test
    @DisplayName("should throw QuotationNotFoundException when quotation not found (404)")
    void shouldThrowQuotationNotFoundExceptionWhen404() {
        // Arrange
        String quotationId = UUID.randomUUID().toString();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            // Simular que el status es 404
            return responseSpec;
        });
        when(responseSpec.bodyToMono(QuotationDTO.class))
                .thenReturn(Mono.error(new QuotationNotFoundException(quotationId)));

        // Act & Assert
        StepVerifier.create(quotationWebClient.findById(quotationId))
                .expectErrorMatches(throwable -> throwable instanceof QuotationNotFoundException &&
                        throwable.getMessage().contains(quotationId))
                .verify();
    }

    @Test
    @DisplayName("should throw QuotationNotFoundException when body is empty")
    void shouldThrowQuotationNotFoundExceptionWhenBodyIsEmpty() {
        // Arrange
        String quotationId = UUID.randomUUID().toString();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(QuotationDTO.class)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(quotationWebClient.findById(quotationId))
                .expectErrorMatches(throwable -> throwable instanceof QuotationNotFoundException &&
                        throwable.getMessage().contains(quotationId))
                .verify();
    }

    @Test
    @DisplayName("should use correct base URL")
    void shouldUseCorrectBaseUrl() {
        // Arrange & Act
        verify(webClientBuilder, times(1)).baseUrl(quotingServiceUrl);
        verify(webClientBuilder, times(1)).build();
    }

    @Test
    @DisplayName("should call correct endpoint with quotation id")
    void shouldCallCorrectEndpointWithQuotationId() {
        // Arrange
        String quotationId = testQuotationDTO.getId();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(QuotationDTO.class)).thenReturn(Mono.just(testQuotationDTO));

        // Act
        quotationWebClient.findById(quotationId).block();

        // Assert
        verify(requestHeadersUriSpec, times(1)).uri("/{id}", quotationId);
    }

    @Test
    @DisplayName("should map DTO to domain correctly")
    void shouldMapDtoToDomainCorrectly() {
        // Arrange
        String quotationId = testQuotationDTO.getId();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(QuotationDTO.class)).thenReturn(Mono.just(testQuotationDTO));

        // Act & Assert
        StepVerifier.create(quotationWebClient.findById(quotationId))
                .assertNext(quotation -> {
                    assertInstanceOf(Quotation.class, quotation);
                    assertEquals(testQuotationDTO.getId(), quotation.id());
                    assertEquals(testQuotationDTO.getPetName(), quotation.petName());
                    assertEquals(testQuotationDTO.getPrice(), quotation.price());
                })
                .verifyComplete();
    }
}
