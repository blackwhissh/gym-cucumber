package com.epam.hibernate.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/v1/sqs/test")
public class TestController {
    private final AmazonSQS sqs;
    @Value("${aws.sqs.url}")
    private String QUEUE_URL;
    public TestController(AmazonSQS sqs) {
        this.sqs = sqs;
    }
    @GetMapping()
    public void test(){
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(QUEUE_URL)
                    .withMessageBody("Hello, this is a test message!")
                    .withDelaySeconds(5);

            sqs.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());

        }
    }
}
