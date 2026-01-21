package com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "quotations")
public class QuotationEntity {

    @Id
    private String id;
    private String petName;
    private String species;
    private String breed;
    private int age;
    private boolean premiumPlan;
    private BigDecimal price;
    private LocalDate expiresAt;

    public QuotationEntity() {
    }

    public QuotationEntity(String id, String petName, String species, String breed, int age, boolean premiumPlan,
            BigDecimal price, LocalDate expiresAt) {
        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.premiumPlan = premiumPlan;
        this.price = price;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
