package com.epam.trainingservice.service.jms;

import com.epam.trainingservice.dto.TrainingInfoMessage;
import com.epam.trainingservice.service.SummaryService;
import com.epam.trainingservice.service.TrainingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@Service
public class TrainingInfoConsumer {
    public static final Logger log = LoggerFactory.getLogger(TrainingInfoConsumer.class);
    private final TrainingService trainingService;
    private final SummaryService summaryService;
    private final ObjectMapper objectMapper;

    public TrainingInfoConsumer(TrainingService trainingService, SummaryService summaryService, ObjectMapper objectMapper) {
        this.summaryService = summaryService;
        this.objectMapper = objectMapper;
        this.trainingService = trainingService;
    }

    @JmsListener(destination = "training.info.queue")
    public void receive(Message<Object> message) throws JsonProcessingException {
        TrainingInfoMessage request = objectMapper.readValue(message.getPayload().toString(), TrainingInfoMessage.class);
        log.info("Received message: " + request.toString());
        trainingService.updateWorkload(request.getUsername(), request.getFirstName(), request.getLastName(),
                request.getActive(), request.getDuration(), request.getTrainingDate(), request.getActionType());
        summaryService.processByUsername(request.getUsername());
    }
}
