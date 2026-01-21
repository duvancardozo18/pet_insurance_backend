package com.pet.insurance.policy_service.domain.port;

import com.pet.insurance.policy_service.domain.model.Policy;
import reactor.core.publisher.Mono;

public interface PolicyRepository {
    Mono<Policy> save(Policy policy);
}
