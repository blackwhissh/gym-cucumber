package com.epam.trainingservice.dto;

public class TrainerSummaryByMonth {
    private int month;
    private int duration;

    public TrainerSummaryByMonth(int month, int duration) {
        this.month = month;
        this.duration = duration;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
