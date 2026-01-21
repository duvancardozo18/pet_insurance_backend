package com.pet.insurance.quoting_service.domain.port;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QuotationRepository {
    Mono<Quotation> save(Quotation quotation);

    Mono<Quotation> findById(String id);

    Flux<Quotation> findAll();
}
