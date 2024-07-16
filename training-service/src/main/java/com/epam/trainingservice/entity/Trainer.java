package com.epam.trainingservice.entity;

import com.epam.trainingservice.entity.enums.ActionType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Trainer {
    @Id
    @SequenceGenerator(name = "trainer_trainer_id_seq", sequenceName = "trainer_trainer_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "trainer_trainer_id_seq", strategy = GenerationType.SEQUENCE)
    private Long trainerId;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Integer totalHours;

    public Trainer() {
    }

    public Trainer(String username, String firstName, String lastName, Boolean isActive, Integer totalHours) {
        this.totalHours = totalHours;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
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

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainerId=" + trainerId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", totalHours=" + totalHours +
                '}';
    }
}
