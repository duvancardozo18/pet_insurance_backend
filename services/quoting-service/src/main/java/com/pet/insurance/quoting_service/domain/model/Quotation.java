package com.pet.insurance.quoting_service.domain.model;

import com.pet.insurance.quoting_service.domain.exception.InvalidPetAgeException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Quotation {

    private static final int MAX_INSURABLE_AGE = 10;
    private static final int QUOTATION_EXPIRATION_DAYS = 30;

    private final String id;
    private final String petName;
    private final String species;
    private final String breed;
    private final int age;
    private final boolean premiumPlan;
    private final BigDecimal price;
    private final LocalDate expiresAt;

    private Quotation(String id, String petName, String species, String breed, int age, boolean premiumPlan,
            BigDecimal price, LocalDate expiresAt) {
        validateAge(age);
        validatePetName(petName);
        validateSpecies(species);
        validatePrice(price);

        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.premiumPlan = premiumPlan;
        this.price = price;
        this.expiresAt = expiresAt;
    }

    public static Quotation create(String petName, String species, String breed, int age, boolean premiumPlan,
            BigDecimal price) {
        return new Quotation(
                UUID.randomUUID().toString(),
                petName,
                species,
                breed,
                age,
                premiumPlan,
                price,
                LocalDate.now().plusDays(QUOTATION_EXPIRATION_DAYS));
    }

    public static Quotation reconstruct(String id, String petName, String species, String breed, int age,
            boolean premiumPlan, BigDecimal price, LocalDate expiresAt) {
        return new Quotation(id, petName, species, breed, age, premiumPlan, price, expiresAt);
    }

    private void validateAge(int age) {
        if (age > MAX_INSURABLE_AGE) {
            throw new InvalidPetAgeException();
        }
        if (age < 0) {
            throw new IllegalArgumentException("Pet age cannot be negative");
        }
    }

    private void validatePetName(String petName) {
        if (petName == null || petName.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty");
        }
    }

    private void validateSpecies(String species) {
        if (species == null || species.trim().isEmpty()) {
            throw new IllegalArgumentException("Species cannot be null or empty");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
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

    public static int getMaxInsurableAge() {
        return MAX_INSURABLE_AGE;
    }
}
