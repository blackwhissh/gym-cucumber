package com.epam.hibernate.cucumber.steps;

import com.epam.hibernate.dto.trainer.request.TrainerRegisterRequest;
import com.epam.hibernate.dto.trainer.request.TrainerTrainingsRequest;
import com.epam.hibernate.dto.trainer.request.UpdateTrainerRequest;
import com.epam.hibernate.dto.trainer.response.TrainerProfileResponse;
import com.epam.hibernate.dto.trainer.response.TrainerRegisterResponse;
import com.epam.hibernate.dto.trainer.response.TrainerTrainingsResponse;
import com.epam.hibernate.dto.trainer.response.UpdateTrainerResponse;
import com.epam.hibernate.entity.TrainingType;
import com.epam.hibernate.service.TrainerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainerManagementStep {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerManagementStep.class);
    private final TrainerService trainerService = mock(TrainerService.class);
    private String username;
    private TrainerRegisterRequest registerRequest;
    private UpdateTrainerRequest updateRequest;
    private TrainerTrainingsRequest trainingsRequest;
    private ResponseEntity<TrainerRegisterResponse> registerResponse;
    private ResponseEntity<TrainerProfileResponse> profileResponse;
    private ResponseEntity<UpdateTrainerResponse> updateResponse;
    private ResponseEntity<List<TrainerTrainingsResponse>> trainingsResponse;


    @Given("I have a trainer registration request")
    public void i_have_a_trainer_registration_request() {
        registerRequest = new TrainerRegisterRequest();
    }

    @When("I create the trainer profile")
    public void i_create_the_trainer_profile() {
        TrainerService trainerService = mock(TrainerService.class);
        when(trainerService.createProfile(registerRequest))
                .thenReturn(ResponseEntity.ok(new TrainerRegisterResponse("test", "test")));
        registerResponse = trainerService.createProfile(registerRequest);
    }

    @Then("the trainer profile should be created successfully")
    public void the_trainer_profile_should_be_created_successfully() {
        assertNotNull(registerResponse);
        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
    }

    @Given("I have a trainer registration request with missing information")
    public void i_have_a_trainer_registration_request_with_missing_information() {
        registerRequest = new TrainerRegisterRequest();
    }

    @When("I attempt to create the trainer profile")
    public void i_attempt_to_create_the_trainer_profile() {
        when(trainerService.createProfile(registerRequest)).thenThrow(new IllegalArgumentException("Missing required information"));
        try {
            registerResponse = trainerService.createProfile(registerRequest);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Captured exception during trainer management scenario" + e);
        }
    }

    @Then("the trainer profile creation should fail with an error")
    public void the_trainer_profile_creation_should_fail_with_an_error() {
        assertNull(registerResponse);
    }

    @Given("I have a username of an existing trainer")
    public void i_have_a_username_of_an_existing_trainer() {
        username = "test.test";
    }

    @When("I select the trainer profile")
    public void i_select_the_trainer_profile() {
        when(trainerService.selectTrainerProfile(username))
                .thenReturn(ResponseEntity.ok(new TrainerProfileResponse("test", "test", new TrainingType(), true, null)));
        profileResponse = trainerService.selectTrainerProfile(username);
    }

    @Then("the trainer profile should be returned successfully")
    public void the_trainer_profile_should_be_returned_successfully() {
        assertNotNull(profileResponse);
        assertEquals(200, profileResponse.getStatusCode().value());
    }

    @Given("I have a username of an existing trainer and an update request")
    public void i_have_a_username_of_an_existing_trainer_and_an_update_request() {
        username = "test.test";
        updateRequest = new UpdateTrainerRequest("John", "Doe", null, true);
    }

    @When("I update the trainer profile")
    public void i_update_the_trainer_profile() {
        when(trainerService.updateTrainer(username,updateRequest))
                .thenReturn(ResponseEntity.ok(new UpdateTrainerResponse("John.Doe","John","Doe",null,true,null)));
        updateResponse = trainerService.updateTrainer(username, updateRequest);
    }

    @Then("the trainer profile should be updated successfully")
    public void the_trainer_profile_should_be_updated_successfully() {
        assertNotNull(updateResponse);
        assertEquals(200, updateResponse.getStatusCode().value());
    }

    @Given("I have a username of an existing trainer and a training request")
    public void i_have_a_username_of_an_existing_trainer_and_a_training_request() {
        username = "trainerUsername";
        trainingsRequest = new TrainerTrainingsRequest(null,null,null);
    }

    @When("I get trainer's training list")
    public void i_get_trainer_training_list() {
        when(trainerService.getTrainingList(username,trainingsRequest))
                .thenReturn(ResponseEntity.ok(
                        List.of(new TrainerTrainingsResponse(
                                "test",
                                Date.from(Instant.now()),
                                new TrainingType(),
                                10,
                                "test"
                        ))));
        trainingsResponse = trainerService.getTrainingList(username,trainingsRequest);
    }

    @Then("trainer's training list should be returned successfully")
    public void trainer_training_list_should_be_returned_successfully() {
        assertNotNull(trainingsResponse);
        assertEquals(200, trainingsResponse.getStatusCode().value());
    }

    @Given("I have a username of a non-existing trainer")
    public void i_have_a_username_of_a_non_existing_trainer() {
        username = "nonExistingUsername";
    }

    @When("I attempt to select trainer profile")
    public void i_select_trainer_profile() {
        when(trainerService.selectTrainerProfile(username)).thenThrow(new IllegalArgumentException("Wrong username"));
        try {
            registerResponse = trainerService.createProfile(registerRequest);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Captured exception during trainer management scenario" + e);
        }
    }

    @Then("the trainer profile selection should fail with an error")
    public void the_trainer_profile_selection_should_fail_with_an_error() {
        assertNull(profileResponse);
    }

    @When("I attempt to update trainer profile")
    public void i_update_trainer_profile() {
        when(trainerService.updateTrainer(username, updateRequest)).thenThrow(new IllegalArgumentException("Wrong username"));
        try {
            updateResponse = trainerService.updateTrainer(username, updateRequest);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Captured exception during trainer management scenario" + e);
        }
    }
    @Then("the trainer profile update should fail with an error")
    public void the_trainer_profile_update_should_fail_with_an_error() {
        assertNull(updateResponse);
    }

}
