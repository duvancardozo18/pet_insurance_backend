package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.controller;

import com.pet.insurance.quoting_service.application.usecase.GenerateQuotationUseCase;
import com.pet.insurance.quoting_service.application.usecase.GetAllQuotationsUseCase;
import com.pet.insurance.quoting_service.application.usecase.GetQuotationByIdUseCase;
import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.web.request.QuotationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;

class QuotationControllerWebFluxTest {

    private WebTestClient webTestClient;

    private GenerateQuotationUseCase generateQuotationUseCase;
    private GetQuotationByIdUseCase getQuotationByIdUseCase;
    private GetAllQuotationsUseCase getAllQuotationsUseCase;

    @BeforeEach
    void setup() {
        generateQuotationUseCase = Mockito.mock(GenerateQuotationUseCase.class);
        getQuotationByIdUseCase = Mockito.mock(GetQuotationByIdUseCase.class);
        getAllQuotationsUseCase = Mockito.mock(GetAllQuotationsUseCase.class);

        QuotationController controller = new QuotationController(
                generateQuotationUseCase,
                getQuotationByIdUseCase,
                getAllQuotationsUseCase);

        webTestClient = WebTestClient.bindToController(controller)
                .configureClient()
                .baseUrl("/")
                .build();
    }

    @Test
    @DisplayName("POST /quotations devuelve la cotización generada")
    void shouldGenerateQuotation() {
        Quotation quotation = Quotation.reconstruct(
                "q-123",
                "Firulais",
                "DOG",
                "Labrador",
                4,
                true,
                BigDecimal.valueOf(26.40),
                LocalDate.now().plusDays(30));

        Mockito.when(generateQuotationUseCase.execute(anyString(), anyString(), anyString(), anyInt(), anyBoolean()))
                .thenReturn(Mono.just(quotation));

        QuotationRequest request = new QuotationRequest("Firulais", "DOG", "Labrador", 4, true);

        webTestClient.post()
                .uri("/quotations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("q-123")
                .jsonPath("$.petName").isEqualTo("Firulais")
                .jsonPath("$.species").isEqualTo("DOG")
                .jsonPath("$.breed").isEqualTo("Labrador")
                .jsonPath("$.age").isEqualTo(4)
                .jsonPath("$.premiumPlan").isEqualTo(true)
                .jsonPath("$.price").isEqualTo(26.40);
    }

    @Test
    @DisplayName("GET /quotations devuelve todas las cotizaciones")
    void shouldGetAllQuotations() {
        Quotation q1 = Quotation.reconstruct("q-1", "Max", "DOG", "Beagle", 2, false, BigDecimal.valueOf(12.00),
                LocalDate.now().plusDays(30));
        Quotation q2 = Quotation.reconstruct("q-2", "Misu", "CAT", "Siames", 6, true, BigDecimal.valueOf(33.00),
                LocalDate.now().plusDays(30));

        Mockito.when(getAllQuotationsUseCase.execute()).thenReturn(Flux.just(q1, q2));

        webTestClient.get()
                .uri("/quotations")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("q-1")
                .jsonPath("$[0].petName").isEqualTo("Max")
                .jsonPath("$[1].id").isEqualTo("q-2")
                .jsonPath("$[1].petName").isEqualTo("Misu");
    }

    @Test
    @DisplayName("GET /quotations/{id} devuelve la cotización por id")
    void shouldGetQuotationById() {
        Quotation quotation = Quotation.reconstruct(
                "q-42",
                "Rocky",
                "DOG",
                "Pitbull",
                3,
                false,
                BigDecimal.valueOf(13.20),
                LocalDate.now().plusDays(30));

        Mockito.when(getQuotationByIdUseCase.execute(eq("q-42"))).thenReturn(Mono.just(quotation));

        webTestClient.get()
                .uri("/quotations/{id}", "q-42")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("q-42")
                .jsonPath("$.petName").isEqualTo("Rocky");
    }
}
