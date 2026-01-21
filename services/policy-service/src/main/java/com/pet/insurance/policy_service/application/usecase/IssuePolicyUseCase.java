package com.pet.insurance.policy_service.application.usecase;

import com.pet.insurance.policy_service.domain.port.PolicyRepository;
import com.pet.insurance.policy_service.domain.port.QuotationClient;
import com.pet.insurance.policy_service.domain.exception.QuotationExpiredException;
import com.pet.insurance.policy_service.domain.model.Owner;
import com.pet.insurance.policy_service.domain.model.Policy;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class IssuePolicyUseCase {

    private final PolicyRepository repository;
    private final QuotationClient quotationClient;

    public IssuePolicyUseCase(PolicyRepository repository, QuotationClient quotationClient) {
        this.repository = repository;
        this.quotationClient = quotationClient;
    }

    public Mono<Policy> execute(
            String quotationId,
            String ownerId,
            String ownerName,
            String ownerEmail) {

        return quotationClient.findById(quotationId)
                .flatMap(quotation -> {
                    if (quotation.isExpired()) {
                        return Mono.error(new QuotationExpiredException(quotationId));
                    }

                    Owner owner = new Owner(ownerId, ownerName, ownerEmail);
                    Policy policy = Policy.issue(UUID.fromString(quotationId), owner);

                    return repository.save(policy);
                });
    }
}
