package com.pet.insurance.policy_service.domain.exception;

public class QuotationExpiredException extends RuntimeException {

    public QuotationExpiredException(String quotationId) {
        super("Quotation has expired with ID: " + quotationId);
    }
}
