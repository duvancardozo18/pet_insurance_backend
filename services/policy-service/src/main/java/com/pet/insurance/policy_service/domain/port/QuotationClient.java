package com.pet.insurance.policy_service.domain.port;

import com.pet.insurance.policy_service.domain.model.Quotation;
import reactor.core.publisher.Mono;

public interface QuotationClient {
    Mono<Quotation> findById(String quotationId);
}
