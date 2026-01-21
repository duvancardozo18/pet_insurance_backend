package com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mapper;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.entity.QuotationEntity;
import org.springframework.stereotype.Component;

@Component
public class QuotationMapper {

    public QuotationEntity toEntity(Quotation quotation) {
        return new QuotationEntity(
                quotation.id(),
                quotation.petName(),
                quotation.species(),
                quotation.breed(),
                quotation.age(),
                quotation.premiumPlan(),
                quotation.price(),
                quotation.expiresAt());
    }

    public Quotation toDomain(QuotationEntity entity) {
        return Quotation.reconstruct(
                entity.getId(),
                entity.getPetName(),
                entity.getSpecies(),
                entity.getBreed(),
                entity.getAge(),
                entity.isPremiumPlan(),
                entity.getPrice(),
                entity.getExpiresAt());
    }

}
