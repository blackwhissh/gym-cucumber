package com.epam.trainingservice.service;

import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.entity.Summary;
import com.epam.trainingservice.service.aws.DynamoDBService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SummaryService {

    private final WorkloadService workloadService;
    private final DynamoDBService dynamoDBService;

    public SummaryService(WorkloadService workloadService, DynamoDBService dynamoDBService) {
        this.workloadService = workloadService;
        this.dynamoDBService = dynamoDBService;
    }

    public void processByUsername(String username) {
        TrainerSummary trainerSummary = workloadService.getTrainerSummary(username);
        Summary summary = new Summary(
                Objects.requireNonNull(trainerSummary).getUsername(),
                trainerSummary.getFirstName(),
                trainerSummary.getLastName(),
                trainerSummary.isStatus(),
                trainerSummary.getYears());

        if (dynamoDBService.findByUsername(username, trainerSummary.isStatus()).isEmpty()) {
            dynamoDBService.save(summary);
        } else {
            dynamoDBService.updateDuration(username, summary);
        }

    }
}
