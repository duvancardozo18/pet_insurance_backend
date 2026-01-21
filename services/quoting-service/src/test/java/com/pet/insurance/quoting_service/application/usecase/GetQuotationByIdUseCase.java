package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetQuotationByIdUseCase {

    private final QuotationRepository repository;

    public GetQuotationByIdUseCase(QuotationRepository repository) {
        this.repository = repository;
    }

    public Mono<Quotation> execute(String id) {
        return repository.findById(id);
    }
}
