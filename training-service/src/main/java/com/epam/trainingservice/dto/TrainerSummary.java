package com.epam.trainingservice.dto;

import java.util.List;

public class TrainerSummary {
    private String username;
    private String firstName;
    private String lastName;
    private boolean status;
    private List<YearSummary> years;
    public TrainerSummary(String username, String firstName, String lastName, boolean status, List<YearSummary> years) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.years = years;
    }

    public TrainerSummary() {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<YearSummary> getYears() {
        return years;
    }

    public void setYears(List<YearSummary> years) {
        this.years = years;
    }

    public static class YearSummary{
        private int year;
        private List<MonthSummary> months;

        public YearSummary(int year, List<MonthSummary> months) {
            this.year = year;
            this.months = months;
        }

        public YearSummary() {
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public List<MonthSummary> getMonths() {
            return months;
        }

        public void setMonths(List<MonthSummary> months) {
            this.months = months;
        }
    }
    public static class MonthSummary {
        private int month;
        private int duration;

        public MonthSummary(int month, int duration) {
            this.month = month;
            this.duration = duration;
        }

        public MonthSummary() {
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
}
