package com.epam.hibernate.service.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.epam.hibernate.dto.TrainingInfoMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SQSService {
    private final AmazonSQS sqsClient;
    @Value("${aws.sqs.url}")
    private String url;

    public SQSService(AmazonSQS sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendMessage(TrainingInfoMessage message){
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(url)
                        .withMessageBody(message.toString())
                                .withDelaySeconds(5);
        sqsClient.sendMessage(sendMessageRequest);
    }

}
