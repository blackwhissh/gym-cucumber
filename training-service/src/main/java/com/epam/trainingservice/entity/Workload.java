package com.epam.trainingservice.entity;

import com.epam.trainingservice.entity.enums.ActionType;
import jakarta.persistence.*;

@Entity
public class Workload {
    @Id
    @SequenceGenerator(name = "workload_workload_id_seq", sequenceName = "workload_workload_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "workload_workload_id_seq", strategy = GenerationType.SEQUENCE)
    private Long workloadId;
    private Integer trainingDuration;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    @ManyToOne
    private Trainer trainer;
    @Column(name = "training_year")
    private Integer year;
    @Column(name = "training_month")
    private Integer month;
    @Column(name = "training_day")
    private Integer day;

    public Workload(Integer year, Integer month, Integer day, Integer trainingDuration, ActionType actionType, Trainer trainer) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.trainingDuration = trainingDuration;
        this.actionType = actionType;
        this.trainer = trainer;
    }

    public Workload() {
    }

    public Long getWorkloadId() {
        return workloadId;
    }

    public void setWorkloadId(Long workloadId) {
        this.workloadId = workloadId;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
