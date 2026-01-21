package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class GenerateQuotationUseCase {

    private final QuotationRepository repository;

    public GenerateQuotationUseCase(QuotationRepository repository) {
        this.repository = repository;
    }

    public Mono<Quotation> execute(
            String petName,
            String species,
            String breed,
            int age,
            boolean premiumPlan) {

        try {
            BigDecimal price = calculatePrice(species, age, premiumPlan);
            Quotation quotation = Quotation.create(petName, species, breed, age, premiumPlan, price);
            return repository.save(quotation);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private BigDecimal calculatePrice(String species, int age, boolean premium) {
        BigDecimal price = BigDecimal.TEN;

        if ("DOG".equalsIgnoreCase(species)) {
            price = price.multiply(BigDecimal.valueOf(1.2));
        } else {
            price = price.multiply(BigDecimal.valueOf(1.1));
        }

        if (age > 5) {
            price = price.multiply(BigDecimal.valueOf(1.5));
        }

        if (premium) {
            price = price.multiply(BigDecimal.valueOf(2));
        }

        return price;
    }
}