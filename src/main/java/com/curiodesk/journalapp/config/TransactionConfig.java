package com.curiodesk.journalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean
    PlatformTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        // implementation of MondoDatabaseFactory is SimpleMongoClientDatabaseFactory, which is a subclass of MongoDatabaseFactory
        // it is used to create a MongoTransactionManager, which is a PlatformTransactionManager implementation for MongoDB
        // SimpleMongoClientDatabaseFactory is configured by Spring Boot using the properties in application.properties or application.yml
        return new MongoTransactionManager(dbFactory);
    }

}
