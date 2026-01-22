package com.pet.insurance.policy_service.domain.exception;

public class QuotationNotFoundException extends RuntimeException {

    public QuotationNotFoundException(String quotationId) {
        super("Quotation not found with ID: " + quotationId);
    }
}
