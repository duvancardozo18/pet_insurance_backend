package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class GenerateQuotationUseCase {

    private static final BigDecimal DOG_PRICE_MULTIPLIER = BigDecimal.valueOf(1.2);
    private static final BigDecimal OTHER_SPECIES_MULTIPLIER = BigDecimal.valueOf(1.1);
    private static final int AGE_THRESHOLD = 5;
    private static final BigDecimal AGE_PREMIUM_MULTIPLIER = BigDecimal.valueOf(1.5);
    private static final BigDecimal PREMIUM_PLAN_MULTIPLIER = BigDecimal.valueOf(2);

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
            price = price.multiply(DOG_PRICE_MULTIPLIER);
        } else {
            price = price.multiply(OTHER_SPECIES_MULTIPLIER);
        }

        if (age > AGE_THRESHOLD) {
            price = price.multiply(AGE_PREMIUM_MULTIPLIER);
        }

        if (premium) {
            price = price.multiply(PREMIUM_PLAN_MULTIPLIER);
        }

        return price;
    }
}