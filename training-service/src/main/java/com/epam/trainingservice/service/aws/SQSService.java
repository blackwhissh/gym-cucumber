package com.epam.trainingservice.service.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.epam.trainingservice.dto.TrainingInfoMessage;
import com.epam.trainingservice.service.SummaryService;
import com.epam.trainingservice.service.TrainingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SQSService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SQSService.class);
    private final AmazonSQS amazonSQS;
    private final TrainingService trainingService;
    private final SummaryService summaryService;
    private final ObjectMapper objectMapper;
    @Value("${aws.sqs.url}")
    private String QUEUE_URL;

    public SQSService(AmazonSQS amazonSQS, TrainingService trainingService, SummaryService summaryService, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.trainingService = trainingService;
        this.summaryService = summaryService;
        this.objectMapper = objectMapper;
    }
    public void receive() throws JsonProcessingException {
        List<Message> messages = amazonSQS.receiveMessage(QUEUE_URL).getMessages();
        Message message = messages.get(0);
        TrainingInfoMessage request = objectMapper.readValue(message.getBody(), TrainingInfoMessage.class);
        LOGGER.info("Received message: " + request.toString());
        trainingService.updateWorkload(request.getUsername(), request.getFirstName(), request.getLastName(),
                request.getActive(), request.getDuration(), request.getTrainingDate(), request.getActionType());
        summaryService.processByUsername(request.getUsername());
        LOGGER.info("Message processed successfully");
    }
}
