package com.pet.insurance.quoting_service.config;

import com.pet.insurance.quoting_service.application.usecase.GenerateQuotationUseCase;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuotationConfig {

    @Bean
    GenerateQuotationUseCase generateQuotationUseCase(
            QuotationRepository repository
    ) {
        return new GenerateQuotationUseCase(repository);
    }
}