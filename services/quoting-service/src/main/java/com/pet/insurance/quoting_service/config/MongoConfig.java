package com.pet.insurance.quoting_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private String port;

    @Bean
    @Primary
    public MongoClient reactiveMongoClient() {
        String connectionString = String.format("mongodb://%s:%s", host, port);
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(connectionString))
                        .build());
    }

    @Bean
    @Primary
    public com.mongodb.client.MongoClient mongoClient() {
        String connectionString = String.format("mongodb://%s:%s", host, port);
        return com.mongodb.client.MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(connectionString))
                        .build());
    }
}
