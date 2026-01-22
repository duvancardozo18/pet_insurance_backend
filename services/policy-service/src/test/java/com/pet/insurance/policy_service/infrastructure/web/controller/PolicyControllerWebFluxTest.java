package com.pet.insurance.policy_service.infrastructure.web.controller;

import com.pet.insurance.policy_service.application.usecase.IssuePolicyUseCase;
import com.pet.insurance.policy_service.domain.exception.QuotationExpiredException;
import com.pet.insurance.policy_service.domain.exception.QuotationNotFoundException;
import com.pet.insurance.policy_service.domain.model.Owner;
import com.pet.insurance.policy_service.domain.model.Policy;
import com.pet.insurance.policy_service.infrastructure.web.exception.GlobalExceptionHandler;
import com.pet.insurance.policy_service.infrastructure.web.request.IssuePolicyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("PolicyController WebFlux tests")
class PolicyControllerWebFluxTest {

    @Mock
    private IssuePolicyUseCase issuePolicyUseCase;

    private WebTestClient webTestClient;

    private String quotationId;
    private Owner owner;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        quotationId = UUID.randomUUID().toString();
        owner = new Owner("owner-123", "John Doe", "john.doe@email.com");

        PolicyController controller = new PolicyController(issuePolicyUseCase);
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("should issue policy successfully")
    void shouldIssuePolicySuccessfully() {
        Policy policy = Policy.issue(UUID.fromString(quotationId), owner);
        when(issuePolicyUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(policy));

        IssuePolicyRequest request = new IssuePolicyRequest(
                quotationId,
                owner.id(),
                owner.name(),
                owner.email());

        webTestClient.post()
                .uri("/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.quotationId").isEqualTo(quotationId)
                .jsonPath("$.active").isEqualTo(true)
                .jsonPath("$.policyId").exists();
    }

    @Test
    @DisplayName("should return 400 when quotation expired")
    void shouldReturnBadRequestWhenQuotationExpired() {
        when(issuePolicyUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new QuotationExpiredException(quotationId)));

        IssuePolicyRequest request = new IssuePolicyRequest(
                quotationId,
                owner.id(),
                owner.name(),
                owner.email());

        webTestClient.post()
                .uri("/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Bad Request")
                .jsonPath("$.message").value(msg -> ((String) msg).contains("Quotation has expired"));
    }

    @Test
    @DisplayName("should return 404 when quotation not found")
    void shouldReturnNotFoundWhenQuotationMissing() {
        when(issuePolicyUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new QuotationNotFoundException(quotationId)));

        IssuePolicyRequest request = new IssuePolicyRequest(
                quotationId,
                owner.id(),
                owner.name(),
                owner.email());

        webTestClient.post()
                .uri("/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found")
                .jsonPath("$.message").value(msg -> ((String) msg).contains(quotationId));
    }
}
