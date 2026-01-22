package com.pet.insurance.policy_service.infrastructure.driven.client;

import com.pet.insurance.policy_service.domain.exception.QuotationNotFoundException;
import com.pet.insurance.policy_service.domain.model.Quotation;
import com.pet.insurance.policy_service.domain.port.QuotationClient;
import com.pet.insurance.policy_service.infrastructure.driven.client.dto.QuotationDTO;
import com.pet.insurance.policy_service.infrastructure.driven.client.mapper.QuotationMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class QuotationWebClient implements QuotationClient {

    private static final int HTTP_NOT_FOUND = 404;

    private final WebClient webClient;
    private final QuotationMapper mapper;

    public QuotationWebClient(
            WebClient.Builder webClientBuilder,
            QuotationMapper mapper,
            @Value("${quoting.service.url}") String quotingServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(quotingServiceUrl).build();
        this.mapper = mapper;
    }

    @Override
    public Mono<Quotation> findById(String quotationId) {
        return webClient
                .get()
                .uri("/{id}", quotationId)
                .retrieve()
                .onStatus(
                        status -> status.value() == HTTP_NOT_FOUND,
                        response -> Mono.error(new QuotationNotFoundException(quotationId)))
                .bodyToMono(QuotationDTO.class)
                .map(mapper::toDomain)
                .switchIfEmpty(Mono.error(new QuotationNotFoundException(quotationId)));
    }
}
