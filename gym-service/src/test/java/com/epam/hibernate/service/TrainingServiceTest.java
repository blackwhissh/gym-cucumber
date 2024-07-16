package com.epam.hibernate.service;

import com.epam.hibernate.dto.AddTrainingRequest;
import com.epam.hibernate.dto.TrainingInfoMessage;
import com.epam.hibernate.dto.user.LoginDTO;
import com.epam.hibernate.entity.*;
import com.epam.hibernate.repository.TraineeRepository;
import com.epam.hibernate.repository.TrainerRepository;
import com.epam.hibernate.repository.TrainingRepository;
import com.epam.hibernate.repository.TrainingTypeRepository;
import com.epam.hibernate.service.jms.TrainingInfoSender;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.io.NotActiveException;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingServiceTest {
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingInfoSender trainingInfoSender;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TrainingService trainingService;

    @Test
    void addTrainingOk() throws NotActiveException {
        Trainee mockTrainee = createMockTrainee();
        Trainer mockTrainer = createMockTrainer();
        when(traineeRepository.selectByUsername(any(String.class))).thenReturn(mockTrainee);
        when(trainerRepository.selectByUsername(any(String.class))).thenReturn(mockTrainer);

        TrainingType mockTrainingType = createMockTrainingType();
        when(trainingTypeRepository.selectByType(any(TrainingTypeEnum.class))).thenReturn(mockTrainingType);

        when(mockTrainer.getSpecialization()).thenReturn(mockTrainingType);

        when(trainerRepository.save(any(Trainer.class))).thenReturn(null);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(null);

        AddTrainingRequest addTrainingRequest = new AddTrainingRequest(new LoginDTO("username", "password"),
                "traineeUsername", "trainerUsername", "test", Date.valueOf("2020-10-10"),
                50, TrainingTypeEnum.AGILITY);
        ResponseEntity<?> responseEntity = trainingService.addTraining(addTrainingRequest);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("Training added successfully", responseEntity.getBody());

    }
    @Test
    public void testGetTrainingTypes() {
        TrainingType trainingType = createMockTrainingType();
        when(trainingTypeRepository.getAll()).thenReturn(List.of(trainingType));
        ResponseEntity<List<TrainingType>> responseEntity = trainingService.getTrainingTypes();
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(List.of(trainingType), responseEntity.getBody());
    }
    @Test
    public void testRemoveTraining() {
        Long trainingId = 1L;
        Training training = getTraining();

        when(trainingRepository.findById(trainingId)).thenReturn(training);

        ResponseEntity<?> result = trainingService.removeTraining(trainingId);

        verify(trainingRepository, times(1)).findById(trainingId);
        verify(trainingRepository, times(1)).delete(trainingId);
        verify(trainingInfoSender, times(1)).send(any(TrainingInfoMessage.class));
        assertEquals("Training removed successfully", result.getBody());
    }

    @NotNull
    private static Training getTraining() {
        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingDate(now());
        training.setTrainingDuration(1);
        return training;
    }

    private TrainingType createMockTrainingType() {
        TrainingType mockTrainingType = mock(TrainingType.class);

        when(mockTrainingType.getTrainingTypeName()).thenReturn(TrainingTypeEnum.AGILITY);

        return mockTrainingType;
    }

    private Trainee createMockTrainee() {
        Trainee mockTrainee = mock(Trainee.class);
        User mockUser = mock(User.class);

        when(mockTrainee.getUser()).thenReturn(mockUser);
        when(mockUser.getActive()).thenReturn(true);
        return mockTrainee;
    }

    private Trainer createMockTrainer() {
        Trainer mockTrainer = mock(Trainer.class);
        User mockUser = mock(User.class);

        when(mockTrainer.getUser()).thenReturn(mockUser);
        when(mockUser.getActive()).thenReturn(true);

        return mockTrainer;
    }


}