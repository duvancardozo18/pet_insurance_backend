package com.pet.insurance.policy_service.infrastructure.driven.persistence.mongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SpringDataPolicyRepository
        extends ReactiveMongoRepository<PolicyDocument, String> {
}
