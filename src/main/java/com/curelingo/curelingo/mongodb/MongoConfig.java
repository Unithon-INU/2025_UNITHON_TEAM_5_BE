package com.curelingo.curelingo.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.curelingo.curelingo.mongodb")
public class MongoConfig {
}
