package com.epam.hibernate.cucumber.steps;

import com.epam.hibernate.dto.AddTrainingRequest;
import com.epam.hibernate.dto.user.LoginDTO;
import com.epam.hibernate.entity.TrainingType;
import com.epam.hibernate.entity.TrainingTypeEnum;
import com.epam.hibernate.service.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.NotActiveException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingManagementStep {
    private final TrainingService trainingService = mock(TrainingService.class);
    private Long trainingId;
    private AddTrainingRequest addTrainingRequest;
    private ResponseEntity<?> addTrainingResponse;
    private ResponseEntity<?> deleteTrainingResponse;
    private ResponseEntity<List<TrainingType>> trainingsListResponse;


    @Given("I have an add training request")
    public void i_have_an_add_training_request() {
        addTrainingRequest = new AddTrainingRequest(
                new LoginDTO("test","test"),
                "trainee",
                "trainer",
                "training",
                Date.from(Instant.now()),
                5,
                TrainingTypeEnum.AGILITY
        );
    }

    @When("I add the training")
    public void i_add_the_training() throws NotActiveException {
        when(trainingService.addTraining(addTrainingRequest))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        addTrainingResponse = trainingService.addTraining(addTrainingRequest);
    }

    @Then("the training should be added successfully")
    public void the_training_should_be_added_successfully() {
        assertNotNull(addTrainingResponse);
        assertEquals(200, addTrainingResponse.getStatusCode().value());
    }

    @When("I get the list of training types")
    public void i_get_the_list_of_training_types() {
        when(trainingService.getTrainingTypes())
                .thenReturn(ResponseEntity.ok(List.of(new TrainingType())));
        trainingsListResponse = trainingService.getTrainingTypes();
    }

    @Then("the list of training types should be returned successfully")
    public void the_list_of_training_types_should_be_returned_successfully() {
        assertNotNull(trainingsListResponse);
        assertEquals(200, trainingsListResponse.getStatusCode().value());
    }

    @Given("I have a training id")
    public void i_have_a_training_id() {
        trainingId = 1L;
    }

    @When("I remove the training")
    public void i_remove_the_training() {
        when(trainingService.removeTraining(trainingId))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        deleteTrainingResponse = trainingService.removeTraining(trainingId);
    }

    @Then("the training should be removed successfully")
    public void the_training_should_be_removed_successfully() {
        assertNotNull(deleteTrainingResponse);
        assertEquals(200, deleteTrainingResponse.getStatusCode().value());
    }
}
