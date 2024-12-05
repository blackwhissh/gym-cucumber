//package com.epam.trainingservice.config;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.sqs.AmazonSQS;
//import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//@Configuration
//@Profile("default")
//public class SQSConfig {
//    @Bean
//    public AmazonSQS amazonSQS() {
//        BasicAWSCredentials awsCreds
//
//        return AmazonSQSClientBuilder.standard()
//                .withRegion("us-east-1")
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                .build();
//    }
//}
