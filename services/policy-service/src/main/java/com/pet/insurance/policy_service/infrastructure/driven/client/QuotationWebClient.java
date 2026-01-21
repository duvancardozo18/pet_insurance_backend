package com.pet.insurance.policy_service.infrastructure.driven.client;

import com.pet.insurance.policy_service.domain.model.Quotation;
import com.pet.insurance.policy_service.domain.port.QuotationClient;
import com.pet.insurance.policy_service.infrastructure.driven.client.dto.QuotationDTO;
import com.pet.insurance.policy_service.infrastructure.driven.client.mapper.QuotationMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class QuotationWebClient implements QuotationClient {

    private final WebClient webClient;
    private final QuotationMapper mapper;

    public QuotationWebClient(WebClient.Builder webClientBuilder, QuotationMapper mapper) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/quotations").build();
        this.mapper = mapper;
    }

    @Override
    public Mono<QuotationDTO> findById(String quotationId) {
        return webClient
                .get()
                .uri("/{id}", quotationId)
                .retrieve()
                .bodyToMono(QuotationDTO.class);
    }
}
