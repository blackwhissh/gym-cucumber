package com.epam.trainingservice.cucumber.steps;

import com.epam.trainingservice.entity.enums.ActionType;
import com.epam.trainingservice.service.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingManagementStep {
    private final TrainingService trainingService = mock(TrainingService.class);
    private String username;
    private String firstName;
    private String lastName;
    private Boolean status;
    private Integer duration;
    private Date trainingDate;
    private ActionType action;
    private boolean response;

    @Given("I have a username, first name, last name, status, duration, training date, and action type")
    public void i_have_a_username_first_name_last_name_status_duration_training_date_and_action_type() {
        username = "trainerUsername";
        firstName = "firstName";
        lastName = "lastName";
        status = true;
        duration = 60;
        trainingDate = new Date();
        action = ActionType.ADD;
    }

    @When("I update the workload")
    public void i_update_the_workload() {
        when(trainingService.updateWorkload(username, firstName, lastName, status, duration, trainingDate, action))
                .thenReturn(true);
        response = trainingService.updateWorkload(username, firstName, lastName, status, duration, trainingDate, action);
    }

    @Then("the workload should be updated successfully")
    public void the_workload_should_be_updated_successfully() {
        assertTrue(response);
    }
}
