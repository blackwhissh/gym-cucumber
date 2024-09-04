package com.epam.trainingservice.controller;

import com.epam.trainingservice.service.aws.DynamoDBService;
import com.epam.trainingservice.service.aws.SQSService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/aws/test")
public class TestController {
    private final DynamoDBService dynamoDBService;
    private final SQSService sqsService;

    public TestController(DynamoDBService dynamoDBService, SQSService sqsService) {
        this.dynamoDBService = dynamoDBService;
        this.sqsService = sqsService;
    }

    @GetMapping("/dynamodb")
    public void testDynamoDb(){
        dynamoDBService.listTables();
    }
    @GetMapping("/sqs")
    public void testSQS() {
        sqsService.receive();
    }
}
