package com.curiodesk.journalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class JournalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalAppApplication.class, args);
    }

}
