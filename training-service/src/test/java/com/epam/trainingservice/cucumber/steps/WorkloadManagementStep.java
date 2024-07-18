package com.epam.trainingservice.cucumber.steps;

import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.dto.TrainerSummaryByMonth;
import com.epam.trainingservice.service.WorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkloadManagementStep {
    private final WorkloadService workloadService = mock(WorkloadService.class);
    private String username;
    private int year;
    private int month;
    private TrainerSummary trainerSummary;
    private TrainerSummaryByMonth trainerSummaryByMonth;
    @Given("I have a username of an existing trainer")
    public void i_have_a_username_of_an_existing_trainer() {
        username = "trainerUsername";
    }

    @When("I get the trainer summary")
    public void i_get_the_trainer_summary() {
        when(workloadService.getTrainerSummary(username))
                .thenReturn(new TrainerSummary());
        trainerSummary = workloadService.getTrainerSummary(username);
    }

    @Then("the trainer summary should be returned successfully")
    public void the_trainer_summary_should_be_returned_successfully() {
        assertNotNull(trainerSummary);
    }

    @Given("I have a username of an existing trainer and a year and a month")
    public void i_have_a_username_of_an_existing_trainer_and_a_year_and_a_month() {
        username = "trainerUsername";
        year = 2022;
        month = 1;
    }

    @When("I get the trainer summary by month and year")
    public void i_get_the_trainer_summary_by_month_and_year() {
        when(workloadService.getTrainerSummaryByMonthAndYear(username, year, month))
                .thenReturn(new TrainerSummaryByMonth(month, 0));
        trainerSummaryByMonth = workloadService.getTrainerSummaryByMonthAndYear(username, year, month);
    }

    @Then("the trainer summary by month and year should be returned successfully")
    public void the_trainer_summary_by_month_and_year_should_be_returned_successfully() {
        assertNotNull(trainerSummaryByMonth);
    }
}
