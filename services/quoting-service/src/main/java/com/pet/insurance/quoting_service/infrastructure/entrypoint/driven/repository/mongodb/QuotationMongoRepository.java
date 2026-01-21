package com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.mongodb;

import com.pet.insurance.quoting_service.infrastructure.entrypoint.driven.repository.entity.QuotationEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationMongoRepository extends ReactiveMongoRepository<QuotationEntity, String> {
}
