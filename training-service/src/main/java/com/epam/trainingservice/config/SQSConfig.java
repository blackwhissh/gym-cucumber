package com.epam.trainingservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//This class is used for local testing purposes
@Configuration
@Profile("default")
public class SQSConfig {
    @Bean
    public AmazonSQS amazonSQS() {

        return AmazonSQSClientBuilder.standard()
                .withRegion("us-east-1")
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
