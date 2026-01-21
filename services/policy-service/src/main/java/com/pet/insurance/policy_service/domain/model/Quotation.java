package com.pet.insurance.policy_service.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Quotation {

    private final String id;
    private final String petName;
    private final String species;
    private final String breed;
    private final int age;
    private final boolean premiumPlan;
    private final BigDecimal price;
    private final LocalDate expiresAt;

    private Quotation(String id, String petName, String species, String breed, int age,
            boolean premiumPlan, BigDecimal price, LocalDate expiresAt) {
        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.premiumPlan = premiumPlan;
        this.price = price;
        this.expiresAt = expiresAt;
    }

    public static Quotation reconstruct(String id, String petName, String species, String breed, int age,
            boolean premiumPlan, BigDecimal price, LocalDate expiresAt) {
        return new Quotation(id, petName, species, breed, age, premiumPlan, price, expiresAt);
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDate.now());
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
}
