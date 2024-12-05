package com.epam.hibernate.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("aws")
public class SQSConfigAWS {
    @Bean
    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}
