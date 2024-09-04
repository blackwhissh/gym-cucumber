package com.epam.trainingservice.service.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.epam.trainingservice.dto.TrainingInfoMessage;
import com.epam.trainingservice.service.SummaryService;
import com.epam.trainingservice.service.TrainingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class SQSService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SQSService.class);
    private final AmazonSQS amazonSQS;
    private final TrainingService trainingService;
    private final SummaryService summaryService;
    @Value("${aws.sqs.url}")
    private String QUEUE_URL;

    public SQSService(AmazonSQS amazonSQS, TrainingService trainingService, SummaryService summaryService) {
        this.amazonSQS = amazonSQS;
        this.trainingService = trainingService;
        this.summaryService = summaryService;
    }

    public void receive() {
        List<Message> messages = amazonSQS.receiveMessage(QUEUE_URL).getMessages();
        if (!messages.isEmpty()) {
            Message message = messages.get(0);
            TrainingInfoMessage request = TrainingInfoMessage.parseFromString(message.getBody());
            LOGGER.info("Received message: " + request);
            trainingService.updateWorkload(request.getUsername(), request.getFirstName(), request.getLastName(), request.getActive(), request.getDuration(), request.getTrainingDate(), request.getActionType());
            summaryService.processByUsername(request.getUsername());
            LOGGER.info("Message processed successfully");
            amazonSQS.deleteMessage(new DeleteMessageRequest(QUEUE_URL, message.getReceiptHandle()));
        }
    }
}
