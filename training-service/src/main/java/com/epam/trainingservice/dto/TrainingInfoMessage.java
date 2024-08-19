package com.epam.trainingservice.dto;

import com.epam.trainingservice.entity.enums.ActionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrainingInfoMessage {
    private String username;
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;
    private Integer duration;
    private ActionType actionType;
    private Boolean isActive;

    public TrainingInfoMessage() {
    }

    public TrainingInfoMessage(String username, String firstName, String lastName, Boolean isActive, Date trainingDate, Integer duration, ActionType actionType) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.trainingDate = trainingDate;
        this.duration = duration;
        this.actionType = actionType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "TrainingInfoRequest{" + "username='" + username + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", trainingDate=" + trainingDate + ", duration=" + duration + ", actionType='" + actionType + '\'' + ", isActive=" + isActive + '}';
    }
    public static TrainingInfoMessage parseFromString(String str) {
        TrainingInfoMessage message = new TrainingInfoMessage();
        str = str.substring(str.indexOf('{') + 1, str.lastIndexOf('}'));
        String[] keyValuePairs = str.split(", ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            try {
                switch (entry[0].trim()) {
                    case "username":
                        message.setUsername(entry[1].trim().replace("'", ""));
                        break;
                    case "firstName":
                        message.setFirstName(entry[1].trim().replace("'", ""));
                        break;
                    case "lastName":
                        message.setLastName(entry[1].trim().replace("'", ""));
                        break;
                    case "isActive":
                        message.setActive(Boolean.parseBoolean(entry[1].trim()));
                        break;
                    case "trainingDate":
                        message.setTrainingDate(dateFormat.parse(entry[1].trim().replace("'", "")));
                        break;
                    case "duration":
                        message.setDuration(Integer.parseInt(entry[1].trim()));
                        break;
                    case "actionType":
                        message.setActionType(ActionType.valueOf(entry[1].trim().replace("'", "")));
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}
