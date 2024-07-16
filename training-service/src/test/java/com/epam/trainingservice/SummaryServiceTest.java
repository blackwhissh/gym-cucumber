package com.epam.trainingservice;

import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.entity.Summary;
import com.epam.trainingservice.repository.SummaryRepository;
import com.epam.trainingservice.service.SummaryService;
import com.epam.trainingservice.service.WorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SummaryServiceTest {
    @Mock
    private SummaryRepository summaryRepository;
    @Mock
    private WorkloadService workloadService;
    @InjectMocks
    private SummaryService summaryService;
    @Test
    public void testProcessByUsername() {
        String username = "testUsername";
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setUsername(username);

        when(workloadService.getTrainerSummary(username)).thenReturn(trainerSummary);
        when(summaryRepository.findByUsername(username)).thenReturn(null);

        summaryService.processByUsername(username);

        verify(workloadService, times(1)).getTrainerSummary(username);
        verify(summaryRepository, times(1)).findByUsername(username);
        verify(summaryRepository, times(1)).save(any(Summary.class));
    }
}
