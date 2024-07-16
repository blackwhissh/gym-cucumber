package com.epam.hibernate.cucumber.steps;

import com.epam.hibernate.dto.trainer.request.TrainerRegisterRequest;
import com.epam.hibernate.dto.trainer.response.TrainerRegisterResponse;
import com.epam.hibernate.service.TrainerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainerManagementStep {
    private TrainerRegisterRequest request;
    private TrainerService trainerService;
    private ResponseEntity<TrainerRegisterResponse> response;

    @Given("I have a trainer registration request")
    public void i_have_a_trainer_registration_request() {
        request = new TrainerRegisterRequest();
    }

    @When("I create the trainer profile")
    public void i_create_the_trainer_profile() {
        TrainerService trainerService = mock(TrainerService.class);
        when(trainerService.createProfile(request))
                .thenReturn(ResponseEntity.ok(new TrainerRegisterResponse("test","test")));
        response = trainerService.createProfile(request);
    }

    @Then("the trainer profile should be created successfully")
    public void the_trainer_profile_should_be_created_successfully() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Given("I have a trainer registration request with missing information")
    public void i_have_a_trainer_registration_request_with_missing_information() {
        request = new TrainerRegisterRequest();
    }

    @When("I attempt to create the trainer profile")
    public void i_attempt_to_create_the_trainer_profile() {
        trainerService = mock(TrainerService.class);
        when(trainerService.createProfile(request)).thenThrow(new IllegalArgumentException("Missing required information"));
        try {
            response = trainerService.createProfile(request);
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Then("the trainer profile creation should fail with an error")
    public void the_trainer_profile_creation_should_fail_with_an_error() {
        assertNull(response);
    }
}
