package com.rest.resource;

import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@SpringBootApplication
@ComponentScan({ "com.rest"})
@EnableMongoRepositories({"com.rest.resource"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    public @Bean Mongo mongo() throws UnknownHostException {
        return new MongoClient();
    }
}