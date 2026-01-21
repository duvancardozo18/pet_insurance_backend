package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetAllQuotationsUseCase {

    private final QuotationRepository repository;

    public GetAllQuotationsUseCase(QuotationRepository repository) {
        this.repository = repository;
    }

    public Flux<Quotation> execute() {
        return repository.findAll();
    }
}
