package com.pet.insurance.quoting_service.domain.exception;


public class InvalidPetAgeException extends RuntimeException {
    public InvalidPetAgeException() {
        super("Pets older than 10 years cannot be insured");
    }
}
