package com.epam.trainingservice.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.epam.trainingservice.dto.TrainerSummary;
import jakarta.persistence.Id;


import java.util.List;
@DynamoDBTable(tableName = "trainer_info")
public class Summary {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private String trainerStatus;
    private List<TrainerSummary.YearSummary> years;

    public Summary() {
    }

    public Summary(String trainerUsername, String trainerFirstName, String trainerLastName, String trainerStatus, List<TrainerSummary.YearSummary> years) {
        this.trainerUsername = trainerUsername;
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.trainerStatus = trainerStatus;
        this.years = years;
    }
    @DynamoDBHashKey(attributeName="trainer_username")
    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }
    @DynamoDBAttribute(attributeName="trainer_first_name")
    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public void setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
    }
    @DynamoDBAttribute(attributeName="trainer_last_name")
    public String getTrainerLastName() {
        return trainerLastName;
    }

    public void setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
    }
    @DynamoDBAttribute(attributeName="trainee_status")

    public String getTrainerStatus() {
        return trainerStatus;
    }

    public void setTrainerStatus(String trainerStatus) {
        this.trainerStatus = trainerStatus;
    }
    @DynamoDBAttribute(attributeName="years")
    public List<TrainerSummary.YearSummary> getYears() {
        return years;
    }

    public void setYears(List<TrainerSummary.YearSummary> years) {
        this.years = years;
    }


}
