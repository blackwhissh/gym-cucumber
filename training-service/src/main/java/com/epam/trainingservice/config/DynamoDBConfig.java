//package com.epam.trainingservice.config;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//@Configuration
//@Profile("default")
//public class DynamoDBConfig {
//    @Bean
//    public AmazonDynamoDB amazonDynamoDB() {
//        BasicAWSCredentials awsCreds
//        return AmazonDynamoDBClientBuilder.standard()
//                .withRegion(Regions.US_EAST_1)
//
//                .build();
//    }
//}
