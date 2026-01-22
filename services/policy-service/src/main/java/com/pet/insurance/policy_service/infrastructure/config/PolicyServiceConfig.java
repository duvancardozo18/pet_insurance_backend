package com.pet.insurance.policy_service.infrastructure.config;

import com.pet.insurance.policy_service.domain.port.PolicyRepository;
import com.pet.insurance.policy_service.domain.port.QuotationClient;
import com.pet.insurance.policy_service.domain.port.DomainEventPublisher;
import com.pet.insurance.policy_service.application.usecase.IssuePolicyUseCase;
import com.pet.insurance.policy_service.infrastructure.driven.client.QuotationWebClient;
import com.pet.insurance.policy_service.infrastructure.driven.client.mapper.QuotationMapper;
import com.pet.insurance.policy_service.infrastructure.driven.persistence.mongo.MongoPolicyRepositoryAdapter;
import com.pet.insurance.policy_service.infrastructure.driven.persistence.mongo.SpringDataPolicyRepository;
import com.pet.insurance.policy_service.infrastructure.event.LoggingEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PolicyServiceConfig {

    @Value("${quoting.service.url:http://localhost:8080}")
    private String quotingServiceUrl;

    @Bean
    PolicyRepository policyRepository(
            SpringDataPolicyRepository mongoRepository) {
        return new MongoPolicyRepositoryAdapter(mongoRepository);
    }

    @Bean
    QuotationMapper quotationMapper() {
        return new QuotationMapper();
    }

    @Bean
    QuotationClient quotationClient(WebClient.Builder webClientBuilder, QuotationMapper quotationMapper) {
        return new QuotationWebClient(webClientBuilder, quotationMapper, quotingServiceUrl);
    }

    @Bean
    DomainEventPublisher domainEventPublisher() {
        return new LoggingEventPublisher();
    }

    @Bean
    IssuePolicyUseCase issuePolicyUseCase(
            PolicyRepository repository,
            QuotationClient quotationClient,
            DomainEventPublisher eventPublisher) {
        return new IssuePolicyUseCase(repository, quotationClient, eventPublisher);
    }
}
