package com.pet.insurance.policy_service.infrastructure.driven.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class QuotationDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("petName")
    private String petName;

    @JsonProperty("species")
    private String species;

    @JsonProperty("breed")
    private String breed;

    @JsonProperty("age")
    private int age;

    @JsonProperty("premiumPlan")
    private boolean premiumPlan;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("expiresAt")
    private LocalDate expiresAt;

    public QuotationDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isPremiumPlan() {
        return premiumPlan;
    }

    public void setPremiumPlan(boolean premiumPlan) {
        this.premiumPlan = premiumPlan;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }
}
