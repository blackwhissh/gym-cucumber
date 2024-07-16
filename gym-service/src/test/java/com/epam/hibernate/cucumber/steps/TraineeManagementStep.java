package com.epam.hibernate.cucumber.steps;

import com.epam.hibernate.dto.trainee.request.TraineeRegisterRequest;
import com.epam.hibernate.dto.trainee.response.TraineeRegisterResponse;
import com.epam.hibernate.dto.trainer.request.TrainerRegisterRequest;
import com.epam.hibernate.service.TraineeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TraineeManagementStep {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeManagementStep.class);
    private final TraineeService traineeService = mock(TraineeService.class);
    private TraineeRegisterRequest registerRequest;
    private ResponseEntity<TraineeRegisterResponse> registerResponse;

    @Given("I have a trainee registration request")
    public void i_have_a_trainer_registration_request() {
        registerRequest = new TraineeRegisterRequest("test", "test", Date.from(Instant.now()), "test");
    }

    @When("I create the trainee profile")
    public void i_create_the_trainer_profile() {
        TraineeService traineeService = mock(TraineeService.class);
        when(traineeService.createProfile(registerRequest))
                .thenReturn(ResponseEntity.ok(new TraineeRegisterResponse("test.test", "test")));
        registerResponse = traineeService.createProfile(registerRequest);
    }

    @Then("the trainee profile should be created successfully")
    public void the_trainer_profile_should_be_created_successfully() {
        assertNotNull(registerResponse);
        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
    }

    @Given("I have a trainee registration request with missing information")
    public void i_have_a_trainer_registration_request_with_missing_information() {
        registerRequest = new TraineeRegisterRequest("test","test", Date.from(Instant.now()),null);
    }

    @When("I attempt to create the trainee profile")
    public void i_attempt_to_create_the_trainer_profile() {
        when(traineeService.createProfile(registerRequest)).thenThrow(new IllegalArgumentException("Missing required information"));
        try {
            registerResponse = traineeService.createProfile(registerRequest);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Captured exception during trainer management scenario" + e);
        }
    }

    @Then("the trainee profile creation should fail with an error")
    public void the_trainer_profile_creation_should_fail_with_an_error() {
        assertNull(registerResponse);
    }
}
