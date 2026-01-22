package com.pet.insurance.quoting_service.domain.exception;

public class QuotationNotFoundException extends RuntimeException {

    public QuotationNotFoundException(String quotationId) {
        super("Quotation not found with ID: " + quotationId);
    }
}
