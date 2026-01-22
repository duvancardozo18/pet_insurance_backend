package com.pet.insurance.policy_service.domain.port;

import com.pet.insurance.policy_service.domain.event.PolicyIssuedEvent;
import reactor.core.publisher.Mono;

/**
 * Port for publishing domain events to external systems.
 * This interface follows the Hexagonal Architecture pattern,
 * allowing the domain to remain independent of infrastructure concerns.
 */
public interface DomainEventPublisher {
    
    /**
     * Publishes a PolicyIssuedEvent when a policy is successfully issued.
     * This event should be consumed by external systems (e.g., billing) to trigger
     * their business processes.
     *
     * @param event the PolicyIssuedEvent to publish
     * @return a Mono that completes when the event has been published
     */
    Mono<Void> publishPolicyIssued(PolicyIssuedEvent event);
}
