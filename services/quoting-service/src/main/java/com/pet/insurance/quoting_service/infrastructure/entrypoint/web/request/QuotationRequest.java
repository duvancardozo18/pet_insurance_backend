package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.request;

public record QuotationRequest(
                String name,
                String species,
                String breed,
                int age,
                boolean premium) {
}
