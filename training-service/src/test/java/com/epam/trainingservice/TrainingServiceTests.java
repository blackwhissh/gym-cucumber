package com.epam.trainingservice;

import com.epam.trainingservice.entity.Trainer;
import com.epam.trainingservice.entity.Workload;
import com.epam.trainingservice.entity.enums.ActionType;
import com.epam.trainingservice.repository.TrainerRepository;
import com.epam.trainingservice.repository.WorkloadRepository;
import com.epam.trainingservice.service.TrainingService;
import com.epam.trainingservice.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static com.epam.trainingservice.utils.DateUtils.*;
import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrainingServiceTests {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private WorkloadRepository workloadRepository;
    @InjectMocks
    private TrainingService trainingService;
    private DateUtils dateUtils;

    @Test
    public void updateWorkloadAddTrainingOkTest(){
        Trainer trainer = mockTrainer();

        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(workloadRepository.save(any(Workload.class))).thenReturn(null);

        boolean result = trainingService.updateWorkload(trainer.getUsername(), trainer.getFirstName(), trainer.getLastName(),
                trainer.getActive(), 4, now(), ActionType.ADD);

        verify(trainerRepository, times(1)).findByUsername(trainer.getUsername());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
        verify(workloadRepository, times(1)).save(any(Workload.class));
        assertTrue(result);
    }
    @Test
    public void updateWorkloadCancelTrainingOkTest(){
        Trainer trainer = mockTrainer();

        Workload workload = new Workload(getYear(now()), getMonth(now()), getDay(now()), 4, ActionType.CANCEL, trainer);
        when(trainerRepository.findByUsername(trainer.getUsername())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(workloadRepository.findFirstByDayAndMonthAndYearAndTrainerAndTrainingDuration(getDay(now()), getMonth(now()), getYear(now()), trainer, 4)).thenReturn(Optional.of(workload));
        when(workloadRepository.save(any(Workload.class))).thenReturn(null);

        boolean result = trainingService.updateWorkload(trainer.getUsername(), trainer.getFirstName(), trainer.getLastName(),
                trainer.getActive(), 4, now(), ActionType.CANCEL);

        verify(trainerRepository, times(1)).findByUsername(trainer.getUsername());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
        verify(workloadRepository, times(1)).findFirstByDayAndMonthAndYearAndTrainerAndTrainingDuration(getDay(now()), getMonth(now()), getYear(now()), trainer, 4);
        verify(workloadRepository, atMost(2)).save(any(Workload.class));
        assertTrue(result);
    }

    @Test
    public void updateWorkloadWrongActionType(){
        assertThrows(IllegalArgumentException.class,
                () -> trainingService.updateWorkload("test", "test", "test",
                        true,4, now(), ActionType.valueOf("test")));
    }

    private Trainer mockTrainer(){
        String username = "testUsername";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        Boolean status = true;

        return new Trainer(username, firstName, lastName, status, 0);
    }

}
