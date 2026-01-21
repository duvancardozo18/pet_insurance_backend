package com.pet.insurance.policy_service.domain.port;

import com.pet.insurance.policy_service.infrastructure.driven.client.dto.QuotationDTO;
import reactor.core.publisher.Mono;

public interface QuotationClient {
    Mono<QuotationDTO> findById(String quotationId);
}
