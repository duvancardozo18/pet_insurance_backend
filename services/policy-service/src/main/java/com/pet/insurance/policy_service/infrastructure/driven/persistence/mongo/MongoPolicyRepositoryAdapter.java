package com.pet.insurance.policy_service.infrastructure.driven.persistence.mongo;

import com.pet.insurance.policy_service.domain.port.PolicyRepository;
import com.pet.insurance.policy_service.domain.model.Owner;
import com.pet.insurance.policy_service.domain.model.Policy;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class MongoPolicyRepositoryAdapter implements PolicyRepository {

    private final SpringDataPolicyRepository repository;

    public MongoPolicyRepositoryAdapter(SpringDataPolicyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Policy> save(Policy policy) {

        PolicyDocument document = toDocument(policy);

        return repository.save(document)
                .map(this::toDomain);
    }

    public Mono<Policy> findById(UUID policyId) {
        return repository.findById(policyId.toString())
                .map(this::toDomain);
    }

    private PolicyDocument toDocument(Policy policy) {
        PolicyDocument doc = new PolicyDocument();

        doc.setId(policy.getId().toString());
        doc.setQuotationId(policy.getQuotationId());

        doc.setOwnerId(policy.getOwner().id());
        doc.setOwnerName(policy.getOwner().name());
        doc.setOwnerEmail(policy.getOwner().email());

        doc.setStartDate(policy.getStartDate());
        doc.setEndDate(policy.getEndDate());
        doc.setActive(policy.isActive());

        return doc;
    }

    private Policy toDomain(PolicyDocument doc) {
        Owner owner = new Owner(
                doc.getOwnerId(),
                doc.getOwnerName(),
                doc.getOwnerEmail());

        return Policy.issue(doc.getQuotationId(), owner);
    }
}
