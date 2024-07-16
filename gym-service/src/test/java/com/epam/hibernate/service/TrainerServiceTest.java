package com.epam.hibernate.service;

import com.epam.hibernate.dto.trainer.request.TrainerRegisterRequest;
import com.epam.hibernate.dto.trainer.request.TrainerTrainingsRequest;
import com.epam.hibernate.dto.trainer.request.UpdateTrainerRequest;
import com.epam.hibernate.dto.trainer.response.TrainerProfileResponse;
import com.epam.hibernate.dto.trainer.response.TrainerRegisterResponse;
import com.epam.hibernate.dto.trainer.response.TrainerTrainingsResponse;
import com.epam.hibernate.dto.trainer.response.UpdateTrainerResponse;
import com.epam.hibernate.dto.user.LoginDTO;
import com.epam.hibernate.entity.*;
import com.epam.hibernate.repository.TrainerRepository;
import com.epam.hibernate.repository.TrainingTypeRepository;
import com.epam.hibernate.repository.UserJpaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerServiceTest {
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TrainerService trainerService;
    @Test
    void createTraineeProfileOk() {
        when(trainerRepository.save(any(Trainer.class))).thenReturn(new Trainer());

        TrainerRegisterRequest validRequest = new TrainerRegisterRequest("John", "Doe", "AGILITY");

        ResponseEntity<TrainerRegisterResponse> responseEntity = trainerService.createProfile(validRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        TrainerRegisterResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getUsername());
        assertNotNull(responseBody.getPassword());
    }

    @Test
    void createProfileSameNameOk() {

        TrainingType mockTrainingType = createMockTrainingType();
        when(trainingTypeRepository.selectByType(any(TrainingTypeEnum.class))).thenReturn(mockTrainingType);

        when(trainerRepository.save(any(Trainer.class))).thenReturn(new Trainer());

        TrainerRegisterRequest request = new TrainerRegisterRequest(
                "John", "Doe", "AGILITY"
        );
        trainerService.createProfile(request);

        assertNotEquals("John.Doe", "John.Doe0");
        verify(trainingTypeRepository, times(1)).selectByType(any(TrainingTypeEnum.class));
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void selectCurrentTrainerProfileOk() {
        Trainer mockTrainer = createMockTrainer();
        when(trainerRepository.selectByUsername(any(String.class))).thenReturn(mockTrainer);

        ResponseEntity<TrainerProfileResponse> responseEntity = trainerService.selectTrainerProfile("John.Trainer");

        assertEquals(200, responseEntity.getStatusCode().value());
        verify(trainerRepository, times(1)).selectByUsername("John.Trainer");
    }

    @Test
    void updateTrainerOk() {
        Trainer mockTrainer = createMockTrainer();

        when(trainerRepository.updateTrainer(any(String.class), any(String.class), any(String.class),
                any(Boolean.class), any(TrainingTypeEnum.class)))
                .thenReturn(mockTrainer);

        UpdateTrainerRequest updateRequest = new UpdateTrainerRequest(
                "John", "Trainer", TrainingTypeEnum.AGILITY, true
        );
        ResponseEntity<UpdateTrainerResponse> responseEntity = trainerService.updateTrainer("John.Trainer", updateRequest);

        assertEquals(200, responseEntity.getStatusCode().value());
        verify(trainerRepository, times(1)).updateTrainer("John.Trainer", updateRequest.getFirstName(), updateRequest.getLastName(), updateRequest.getActive(), updateRequest.getSpecialization());
    }

    @Test
    void getTrainingListOk() {
        Trainer mockTrainer = createMockTrainer();

        when(trainerRepository.selectByUsername(any(String.class))).thenReturn(mockTrainer);

        List<Training> mockTrainingList = createMockTrainingList();
        when(trainerRepository.getTrainingList(any(String.class), any(Date.class), any(Date.class),
                any(String.class), any(TrainingTypeEnum.class))).thenReturn(mockTrainingList);

        TrainerTrainingsRequest trainingsRequest = new TrainerTrainingsRequest(
                null, null, "traineeName");
        ResponseEntity<List<TrainerTrainingsResponse>> responseEntity = trainerService.getTrainingList("username", trainingsRequest);

        assertEquals(200, responseEntity.getStatusCode().value());
    }


    private List<Training> createMockTrainingList() {
        Training mockTraining = mock(Training.class);
        return List.of(mockTraining);
    }
    private TrainingType createMockTrainingType() {
        TrainingType trainingType = mock(TrainingType.class);

        when(trainingType.getTrainingTypeName()).thenReturn(TrainingTypeEnum.AGILITY);

        return trainingType;
    }

    private Trainer createMockTrainer() {
        Trainer mockTrainer = mock(Trainer.class);
        User mockUser = mock(User.class);
        TrainingType trainingType = createMockTrainingType();
        when(mockTrainer.getUser()).thenReturn(mockUser);
        when(mockTrainer.getSpecialization()).thenReturn(trainingType);
        return mockTrainer;
    }
}