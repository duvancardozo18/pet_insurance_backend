package com.pet.insurance.quoting_service.domain.exception;

import com.pet.insurance.quoting_service.domain.model.Quotation;

public class InvalidPetAgeException extends RuntimeException {
    public InvalidPetAgeException() {
        super("Pets older than " + Quotation.getMaxInsurableAge() + " years cannot be insured");
    }
}
