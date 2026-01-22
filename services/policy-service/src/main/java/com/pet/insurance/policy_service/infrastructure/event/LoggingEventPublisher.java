package com.pet.insurance.policy_service.infrastructure.event;

import com.pet.insurance.policy_service.domain.event.PolicyIssuedEvent;
import com.pet.insurance.policy_service.domain.port.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Infrastructure adapter that simulates event publishing by logging events.
 */
public class LoggingEventPublisher implements DomainEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(LoggingEventPublisher.class);

    @Override
    public Mono<Void> publishPolicyIssued(PolicyIssuedEvent event) {
        return Mono.fromRunnable(() -> {
            logger.info("========================================");
            logger.info("DOMAIN EVENT PUBLISHED: PolicyIssuedEvent");
            logger.info("Policy ID: {}", event.policyId());
            logger.info("Quotation ID: {}", event.quotationId());
            logger.info("Owner Email: {}", event.ownerEmail());
            logger.info("Timestamp: {}", java.time.Instant.now());
            logger.info("========================================");
            logger.info("This event would be sent to external billing system for invoice generation");
        });
    }
}
