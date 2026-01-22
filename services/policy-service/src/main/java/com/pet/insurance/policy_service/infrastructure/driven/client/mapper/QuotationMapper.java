package com.pet.insurance.policy_service.infrastructure.driven.client.mapper;

import com.pet.insurance.policy_service.domain.model.Quotation;
import com.pet.insurance.policy_service.infrastructure.driven.client.dto.QuotationDTO;
import org.springframework.stereotype.Component;

@Component
public class QuotationMapper {

    public Quotation toDomain(QuotationDTO dto) {
        return Quotation.reconstruct(
                dto.getId(),
                dto.getPetName(),
                dto.getSpecies(),
                dto.getBreed(),
                dto.getAge(),
                dto.isPremiumPlan(),
                dto.getPrice(),
                dto.getExpiresAt());
    }
}
