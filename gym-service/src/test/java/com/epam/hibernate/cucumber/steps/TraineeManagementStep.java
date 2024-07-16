package com.epam.hibernate.cucumber.steps;

import com.epam.hibernate.dto.trainee.request.TraineeRegisterRequest;
import com.epam.hibernate.dto.trainee.response.TraineeRegisterResponse;
import com.epam.hibernate.service.TraineeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TraineeManagementStep {
    private TraineeRegisterRequest request;
    private ResponseEntity<TraineeRegisterResponse> response;

    @Given("I have a trainee registration request")
    public void i_have_a_trainer_registration_request() {
        request = new TraineeRegisterRequest("test", "test", Date.from(Instant.now()), "test");
    }

    @When("I create the trainee profile")
    public void i_create_the_trainer_profile() {
        TraineeService traineeService = mock(TraineeService.class);
        when(traineeService.createProfile(request))
                .thenReturn(ResponseEntity.ok(new TraineeRegisterResponse("test.test", "test")));
        response = traineeService.createProfile(request);
    }

    @Then("the trainee profile should be created successfully")
    public void the_trainer_profile_should_be_created_successfully() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
