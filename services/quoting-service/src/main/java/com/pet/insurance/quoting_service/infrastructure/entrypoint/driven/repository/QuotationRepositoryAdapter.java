package com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mapper.QuotationMapper;
import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mongodb.QuotationMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class QuotationRepositoryAdapter implements QuotationRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuotationRepositoryAdapter.class);

    private final QuotationMongoRepository mongoRepository;
    private final QuotationMapper mapper;

    public QuotationRepositoryAdapter(QuotationMongoRepository mongoRepository, QuotationMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Quotation> save(Quotation quotation) {
        return Mono.just(quotation)
                .map(mapper::toEntity)
                .flatMap(mongoRepository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Quotation> findById(String id) {
        return mongoRepository.findById(id)
                .flatMap(entity -> {
                    try {
                        return Mono.just(mapper.toDomain(entity));
                    } catch (IllegalArgumentException e) {
                        logger.error("Invalid quotation record with id: {} - {}", id, e.getMessage());
                        return Mono.error(e);
                    }
                });
    }

    @Override
    public Flux<Quotation> findAll() {
        return mongoRepository.findAll()
                .flatMap(entity -> {
                    try {
                        return Mono.just(mapper.toDomain(entity));
                    } catch (IllegalArgumentException e) {
                        logger.warn("Skipping invalid quotation record with id: {} - {}",
                                entity.getId(), e.getMessage());
                        return Mono.empty();
                    }
                });
    }
}
