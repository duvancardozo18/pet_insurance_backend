package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.controller;

import com.pet.insurance.quoting_service.application.usecase.GenerateQuotationUseCase;
import com.pet.insurance.quoting_service.application.usecase.GetAllQuotationsUseCase;
import com.pet.insurance.quoting_service.application.usecase.GetQuotationByIdUseCase;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.web.dto.QuotationDTO;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.web.request.QuotationRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/quotations")
public class QuotationController {

    private final GenerateQuotationUseCase generateQuotationUseCase;
    private final GetQuotationByIdUseCase getQuotationByIdUseCase;
    private final GetAllQuotationsUseCase getAllQuotationsUseCase;

    public QuotationController(GenerateQuotationUseCase generateQuotationUseCase,
            GetQuotationByIdUseCase getQuotationByIdUseCase,
            GetAllQuotationsUseCase getAllQuotationsUseCase) {
        this.generateQuotationUseCase = generateQuotationUseCase;
        this.getQuotationByIdUseCase = getQuotationByIdUseCase;
        this.getAllQuotationsUseCase = getAllQuotationsUseCase;
    }

    @PostMapping
    public Mono<QuotationDTO> generate(@RequestBody QuotationRequest request) {
        return generateQuotationUseCase.execute(
                request.name(),
                request.species(),
                request.breed(),
                request.age(),
                request.premium())
                .map(QuotationDTO::fromDomain);
    }

    @GetMapping
    public Flux<QuotationDTO> getAll() {
        return getAllQuotationsUseCase.execute()
                .map(QuotationDTO::fromDomain);
    }

    @GetMapping("/{id}")
    public Mono<QuotationDTO> getById(@PathVariable String id) {
        return getQuotationByIdUseCase.execute(id)
                .map(QuotationDTO::fromDomain);
    }

}
