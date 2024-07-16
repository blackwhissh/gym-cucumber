package com.epam.hibernate.service;

import com.epam.hibernate.dto.trainee.request.TraineeRegisterRequest;
import com.epam.hibernate.dto.trainee.request.TraineeTrainingsRequest;
import com.epam.hibernate.dto.trainee.request.UpdateTraineeRequest;
import com.epam.hibernate.dto.trainee.request.UpdateTrainersListRequest;
import com.epam.hibernate.dto.trainee.response.*;
import com.epam.hibernate.dto.trainer.TrainerListInfo;
import com.epam.hibernate.entity.*;
import com.epam.hibernate.repository.TraineeRepository;
import com.epam.hibernate.repository.TrainerRepository;
import com.epam.hibernate.repository.UserJpaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TraineeServiceTest {
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private UserJpaRepository userJpaRepository;
    @InjectMocks
    private TraineeService traineeService;


    @Test
    void createTraineeProfileOk() {
        when(traineeRepository.save(any(Trainee.class))).thenReturn(new Trainee());

        TraineeRegisterRequest validRequest = new TraineeRegisterRequest("John", "Doe", Date.valueOf("2001-10-10"), "123 Main St");

        ResponseEntity<TraineeRegisterResponse> responseEntity = traineeService.createProfile(validRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        TraineeRegisterResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getUsername());
        assertNotNull(responseBody.getPassword());
    }

    @Test
    void createTraineeProfileSameNameOk() {
        when(traineeRepository.save(any(Trainee.class))).thenReturn(new Trainee());

        TraineeRegisterRequest validRequest = new TraineeRegisterRequest("John", "Doe", Date.valueOf("2001-10-10"), "123 Main St");

        ResponseEntity<TraineeRegisterResponse> responseEntity = traineeService.createProfile(validRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        TraineeRegisterResponse responseBody = responseEntity.getBody();

        assertNotNull(responseBody);
        assertNotNull(responseBody.getUsername());
        assertNotNull(responseBody.getPassword());

        assertNotNull(responseBody);
        assertNotNull(responseBody.getUsername());
        assertNotNull(responseBody.getPassword());

        assertEquals("John.Doe0", responseBody.getUsername());
    }

    @Test
    void selectTraineeProfileOk() {
        Trainee mockTrainee = createMockTrainee();
        when(traineeRepository.selectByUsername(any(String.class))).thenReturn(mockTrainee);

        ResponseEntity<TraineeProfileResponse> response = traineeService.selectTraineeProfile("John.Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateTraineeProfileOk() {
        Trainee mockTrainee = createMockTrainee();
        when(traineeRepository.updateTrainee(any(String.class), any(), any(), any(), any(), any())).thenReturn(mockTrainee);

        ResponseEntity<UpdateTraineeResponse> responseEntity = traineeService.updateTrainee("John.Doe", new UpdateTraineeRequest("James", "Smith", null, null, true));

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getTrainingListOk() {
        List<Training> mockTrainingList = createMockTrainingList();
        when(traineeRepository.getTrainingList(anyString(), any(), any(), any(), any())).thenReturn(mockTrainingList);

        TraineeTrainingsRequest request = new TraineeTrainingsRequest(null, null, null, null);
        ResponseEntity<List<TraineeTrainingsResponse>> responseEntity = traineeService.getTrainingList("John.Doe", request);

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void notAssignedTrainersListOk() {
        Trainee mockTrainee = createMockTrainee();
        when(traineeRepository.selectByUsername(any(String.class))).thenReturn(mockTrainee);

        List<Trainer> mockTrainers = createMockTrainerList();
        when(trainerRepository.getAllTrainers()).thenReturn(mockTrainers);

        ResponseEntity<List<NotAssignedTrainer>> responseEntity = traineeService.notAssignedTrainersList("John.Doe");

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void updateTrainersListOk() {
        Trainee mockTrainee = createMockTrainee();
        when(traineeRepository.selectByUsername(any(String.class))).thenReturn(mockTrainee);

        Trainer mockTrainer = createMockTrainer();
        when(trainerRepository.selectByUsername(any(String.class))).thenReturn(mockTrainer);

        when(traineeRepository.save(any(Trainee.class))).thenReturn(new Trainee());

        Set<String> trainersSet = new HashSet<>();
        trainersSet.add("trainerUsername");
        UpdateTrainersListRequest request = new UpdateTrainersListRequest(trainersSet);

        request.setTrainers(trainersSet);

        ResponseEntity<List<TrainerListInfo>> responseEntity = traineeService.updateTrainersList("John.Doe", request);

        assertEquals(200, responseEntity.getStatusCode().value());
        verify(trainerRepository, times(1)).selectByUsername("trainerUsername");
    }

    @Test
    public void assignedTrainersListTest(){
        String traineeUsername = "testTraineeUsername";
        Trainee trainee = createMockTrainee();
        trainee.getUser().setUsername(traineeUsername);

        Trainer trainer = new Trainer();
        trainer.setTrainees(Set.of(trainee));

        List<Trainer> allTrainers = Arrays.asList(trainer);

        when(traineeRepository.selectByUsername(traineeUsername)).thenReturn(trainee);
        when(trainerRepository.getAllTrainers()).thenReturn(allTrainers);

        List<Trainer> result = traineeService.assignedTrainersList(traineeUsername);

        verify(traineeRepository, times(1)).selectByUsername(traineeUsername);
        verify(trainerRepository, times(1)).getAllTrainers();
        assertEquals(1, result.size());
        assertEquals(trainer, result.get(0));
    }


    private Trainee createMockTrainee() {
        Trainee mockTrainee = mock(Trainee.class);
        User mockUser = mock(User.class);

        when(mockTrainee.getUser()).thenReturn(mockUser);

        return mockTrainee;
    }

    private Trainer createMockTrainer() {
        Trainer mockTrainer = mock(Trainer.class);
        User mockUser = mock(User.class);

        when(mockUser.getActive()).thenReturn(true);
        when(mockTrainer.getUser()).thenReturn(mockUser);

        return mockTrainer;
    }

    private List<Trainer> createMockTrainerList() {
        Trainer trainer = mock(Trainer.class);

        when(trainer.getUser()).thenReturn(new User("John", "Doe", true, RoleEnum.TRAINER));
        return List.of(trainer);
    }

    private List<Training> createMockTrainingList() {
        Training training = mock(Training.class);

        when(training.getTrainingDate()).thenReturn(Date.valueOf("2024-10-10"));
        when(training.getTrainingName()).thenReturn("test");
        when(training.getTrainingDuration()).thenReturn(10);
        when(training.getTrainingType()).thenReturn(new TrainingType());
        when(training.getTrainer()).thenReturn(new Trainer(new TrainingType(TrainingTypeEnum.AGILITY), new User("John", "Doe", true, RoleEnum.TRAINEE)));

        return List.of(training);
    }
}