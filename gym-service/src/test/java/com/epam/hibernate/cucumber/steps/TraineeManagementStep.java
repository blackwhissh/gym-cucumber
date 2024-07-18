package com.epam.hibernate.cucumber.steps;

import com.epam.hibernate.dto.trainee.request.TraineeRegisterRequest;
import com.epam.hibernate.dto.trainee.request.TraineeTrainingsRequest;
import com.epam.hibernate.dto.trainee.request.UpdateTraineeRequest;
import com.epam.hibernate.dto.trainee.request.UpdateTrainersListRequest;
import com.epam.hibernate.dto.trainee.response.*;
import com.epam.hibernate.dto.trainer.TrainerListInfo;
import com.epam.hibernate.dto.trainer.response.UpdateTrainerResponse;
import com.epam.hibernate.entity.Trainer;
import com.epam.hibernate.entity.TrainingType;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TraineeManagementStep {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeManagementStep.class);
    private final TraineeService traineeService = mock(TraineeService.class);
    private String username;
    private TraineeRegisterRequest registerRequest;
    private UpdateTraineeRequest updateRequest;
    private TraineeTrainingsRequest trainingsRequest;
    private UpdateTrainersListRequest trainersRequest;
    private ResponseEntity<TraineeRegisterResponse> registerResponse;
    private ResponseEntity<TraineeProfileResponse> profileResponse;
    private ResponseEntity<UpdateTraineeResponse> updateResponse;
    private ResponseEntity<List<TraineeTrainingsResponse>> trainingsResponse;
    private ResponseEntity<List<NotAssignedTrainer>> trainersResponse;
    private List<Trainer> assignedTrainers;
    private ResponseEntity<List<TrainerListInfo>> trainerListResponse;

    @Given("I have a trainee registration request")
    public void i_have_a_trainee_registration_request() {
        registerRequest = new TraineeRegisterRequest("test", "test", Date.from(Instant.now()), "test");
    }

    @When("I create the trainee profile")
    public void i_create_the_trainee_profile() {
        TraineeService traineeService = mock(TraineeService.class);
        when(traineeService.createProfile(registerRequest))
                .thenReturn(ResponseEntity.ok(new TraineeRegisterResponse("test.test", "test")));
        registerResponse = traineeService.createProfile(registerRequest);
    }

    @Then("the trainee profile should be created successfully")
    public void the_trainee_profile_should_be_created_successfully() {
        assertNotNull(registerResponse);
        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
    }

    @Given("I have a trainee registration request with missing information")
    public void i_have_a_trainee_registration_request_with_missing_information() {
        registerRequest = new TraineeRegisterRequest("test", "test", Date.from(Instant.now()), null);
    }

    @When("I attempt to create the trainee profile")
    public void i_attempt_to_create_the_trainee_profile() {
        when(traineeService.createProfile(registerRequest)).thenThrow(new IllegalArgumentException("Missing required information"));
        try {
            registerResponse = traineeService.createProfile(registerRequest);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Captured exception during trainer management scenario" + e);
        }
    }

    @Then("the trainee profile creation should fail with an error")
    public void the_trainee_profile_creation_should_fail_with_an_error() {
        assertNull(registerResponse);
    }

    @Given("I have a username of an existing trainee")
    public void i_have_a_username_of_an_existing_trainee() {
        username = "test.test";
    }

    @When("I select the trainee profile")
    public void i_select_the_trainee_profile() {
        when(traineeService.selectTraineeProfile(username))
                .thenReturn(ResponseEntity.ok(new TraineeProfileResponse("test", "test", Date.from(Instant.now()), "test", true, null)));
        profileResponse = traineeService.selectTraineeProfile(username);
    }

    @Then("the trainee profile should be returned successfully")
    public void the_trainee_profile_should_be_returned_successfully() {
        assertNotNull(profileResponse);
        assertEquals(200, profileResponse.getStatusCode().value());
    }

    @Given("I have a username of an existing trainee and an update request")
    public void i_have_a_username_of_an_existing_trainee_and_an_update_request() {
        username = "test.test";
        updateRequest = new UpdateTraineeRequest("John", "Doe", null, "test", true);
    }

    @When("I update the trainee profile")
    public void i_update_the_trainee_profile() {
        when(traineeService.updateTrainee(username, updateRequest))
                .thenReturn(ResponseEntity.ok(new UpdateTraineeResponse("John.Doe", "John", "Doe", null, null, true, null)));
        updateResponse = traineeService.updateTrainee(username, updateRequest);
    }

    @Then("the trainee profile should be updated successfully")
    public void the_trainee_profile_should_be_updated_successfully() {
        assertNotNull(updateResponse);
        assertEquals(200, updateResponse.getStatusCode().value());
    }

    @When("I delete the trainee profile")
    public void i_delete_the_trainee_profile() {
        doNothing().when(traineeService).deleteTrainee(username);
        traineeService.deleteTrainee(username);
    }
    @Then("the trainee profile should be deleted successfully")
    public void the_trainee_profile_should_be_deleted_successfully() {
        verify(traineeService).deleteTrainee(username);
    }

    @Given("I have a username of an existing trainee and a training request")
    public void i_have_a_username_of_an_existing_trainee_and_a_training_request() {
        username = "traineeUsername";
        trainingsRequest = new TraineeTrainingsRequest(null, null, null, null);
    }

    @When("I get trainee's training list")
    public void i_get_trainee_training_list() {
        when(traineeService.getTrainingList(username, trainingsRequest))
                .thenReturn(ResponseEntity.ok(List.of(new TraineeTrainingsResponse(
                        "test",
                        Date.from(Instant.now()),
                        new TrainingType(),
                        5,
                        "test"
                        ))));
        trainingsResponse = traineeService.getTrainingList(username, trainingsRequest);
    }

    @Then("trainee's training list should be returned successfully")
    public void trainee_training_list_should_be_returned_successfully() {
        assertNotNull(trainingsResponse);
        assertEquals(200, trainingsResponse.getStatusCode().value());
    }

    @When("I get the list of not assigned trainers")
    public void i_get_the_list_of_not_assigned_trainers() {
        when(traineeService.notAssignedTrainersList(username))
                .thenReturn(ResponseEntity.ok(List.of(new NotAssignedTrainer("test", "test", "test",new TrainingType()))));
        trainersResponse = traineeService.notAssignedTrainersList(username);
    }

    @Then("the list of not assigned trainers should be returned successfully")
    public void the_list_of_not_assigned_trainers_should_be_returned_successfully() {
        assertNotNull(trainersResponse);
        assertEquals(200, trainersResponse.getStatusCode().value());
    }
    @When("I get the list of assigned trainers")
    public void i_get_the_list_of_assigned_trainers() {
        when(traineeService.assignedTrainersList(username))
                .thenReturn(List.of(new Trainer()));
        assignedTrainers = traineeService.assignedTrainersList(username);
    }

    @Then("the list of assigned trainers should be returned successfully")
    public void the_list_of_assigned_trainers_should_be_returned_successfully() {
        assertNotNull(assignedTrainers);
    }

    @Given("I have a username of an existing trainee and a list of trainers")
    public void i_have_a_username_of_an_existing_trainee_and_a_list_of_trainers() {
        username = "traineeUsername";
        trainersRequest = new UpdateTrainersListRequest(Set.of("trainer"));
    }

    @When("I update the list of trainers")
    public void i_update_the_list_of_trainers() {
        when(traineeService.updateTrainersList(username, trainersRequest))
                .thenReturn(ResponseEntity.ok(List.of(new TrainerListInfo("test","test","test",new TrainingType()))));
        trainerListResponse = traineeService.updateTrainersList(username, trainersRequest);
    }

    @Then("the list of trainers should be updated successfully")
    public void the_list_of_trainers_should_be_updated_successfully() {
        assertNotNull(trainerListResponse);
        assertEquals(200, trainerListResponse.getStatusCode().value());
    }

}
