package com.epam.trainingservice;

import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.dto.TrainerSummaryByMonth;
import com.epam.trainingservice.entity.Trainer;
import com.epam.trainingservice.entity.Workload;
import com.epam.trainingservice.entity.enums.ActionType;
import com.epam.trainingservice.repository.TrainerRepository;
import com.epam.trainingservice.repository.WorkloadRepository;
import com.epam.trainingservice.service.WorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WorkloadServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private WorkloadRepository workloadRepository;
    @InjectMocks
    private WorkloadService workloadService;

    @Test
    public void testGetTrainerSummary() {
        // Arrange
        String username = "testUsername";
        Trainer trainer = new Trainer(username, "testFirstName", "testLastName", true, 1);
        Workload workload = new Workload(2022, 1, 1, 1, ActionType.ADD, trainer);
        List<Workload> workloads = Arrays.asList(workload);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(workloadRepository.findByTrainerOrderByYearAsc(trainer)).thenReturn(workloads);

        TrainerSummary result = workloadService.getTrainerSummary(username);

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(workloadRepository, times(1)).findByTrainerOrderByYearAsc(trainer);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }
    @Test
    public void testGetTrainerSummaryByMonthAndYear() {
        String username = "testUsername";
        int year = 2022;
        int month = 1;
        Trainer trainer = new Trainer(username, "testFirstName", "testLastName", true, 1);
        Workload workload = new Workload(year, month, 1, 1, ActionType.ADD, trainer);
        List<Workload> workloads = Arrays.asList(workload);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(workloadRepository.findByTrainerOrderByYearAsc(trainer)).thenReturn(workloads);

        TrainerSummaryByMonth result = workloadService.getTrainerSummaryByMonthAndYear(username, year, month);

        verify(trainerRepository, times(1)).findByUsername(username);
        verify(workloadRepository, times(1)).findByTrainerOrderByYearAsc(trainer);
        assertNotNull(result);
        assertEquals(month, result.getMonth());
        assertEquals(workload.getTrainingDuration(), result.getDuration());
    }
}
