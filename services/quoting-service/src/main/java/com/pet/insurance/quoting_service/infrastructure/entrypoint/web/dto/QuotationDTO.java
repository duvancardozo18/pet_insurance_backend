package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.insurance.quoting_service.domain.model.Quotation;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class QuotationDTO {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("petName")
    private final String petName;

    @JsonProperty("species")
    private final String species;

    @JsonProperty("breed")
    private final String breed;

    @JsonProperty("age")
    private final int age;

    @JsonProperty("premiumPlan")
    private final boolean premiumPlan;

    @JsonProperty("price")
    private final BigDecimal price;

    @JsonProperty("expiresAt")
    private final LocalDate expiresAt;

    @JsonProperty("expired")
    private final boolean expired;

    private QuotationDTO(String id, String petName, String species, String breed, int age,
            boolean premiumPlan, BigDecimal price, LocalDate expiresAt, boolean expired) {
        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.premiumPlan = premiumPlan;
        this.price = price;
        this.expiresAt = expiresAt;
        this.expired = expired;
    }

    public static QuotationDTO fromDomain(Quotation quotation) {
        return new QuotationDTO(
                quotation.id(),
                quotation.petName(),
                quotation.species(),
                quotation.breed(),
                quotation.age(),
                quotation.premiumPlan(),
                quotation.price(),
                quotation.expiresAt(),
                quotation.isExpired());
    }

    public String id() {
        return id;
    }

    public String petName() {
        return petName;
    }

    public String species() {
        return species;
    }

    public String breed() {
        return breed;
    }

    public int age() {
        return age;
    }

    public boolean premiumPlan() {
        return premiumPlan;
    }

    public BigDecimal price() {
        return price;
    }

    public LocalDate expiresAt() {
        return expiresAt;
    }

    public boolean expired() {
        return expired;
    }
}
