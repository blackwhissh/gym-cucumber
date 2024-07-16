package com.epam.trainingservice.service;

import com.epam.trainingservice.dto.TrainerSummary;
import com.epam.trainingservice.dto.TrainerSummaryByMonth;
import com.epam.trainingservice.entity.Trainer;
import com.epam.trainingservice.entity.Workload;
import com.epam.trainingservice.entity.enums.ActionType;
import com.epam.trainingservice.exception.TrainerNotFoundException;
import com.epam.trainingservice.repository.TrainerRepository;
import com.epam.trainingservice.repository.WorkloadRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.epam.trainingservice.dto.TrainerSummary.MonthSummary;
import static com.epam.trainingservice.dto.TrainerSummary.YearSummary;

@Service
public class WorkloadService {
    private final WorkloadRepository workloadRepository;
    private final TrainerRepository trainerRepository;

    public WorkloadService(WorkloadRepository workloadRepository, TrainerRepository trainerRepository) {
        this.workloadRepository = workloadRepository;
        this.trainerRepository = trainerRepository;
    }

    public TrainerSummary getTrainerSummary(String username) {
        Trainer trainer = trainerRepository.findByUsername(username).orElseThrow(TrainerNotFoundException::new);
        List<Workload> workloads = workloadRepository.findByTrainerOrderByYearAsc(trainer);


        TrainerSummary summary = new TrainerSummary();
        summary.setUsername(trainer.getUsername());
        summary.setFirstName(trainer.getFirstName());
        summary.setLastName(trainer.getLastName());
        summary.setStatus(trainer.getActive());
        summary.setYears(new ArrayList<>());

        for (Workload workload : workloads) {
            addWorkloadToSummary(summary, workload);
        }

        return summary;

    }

    private void addWorkloadToSummary(TrainerSummary summary, Workload workload) {
        if (workload.getActionType() == ActionType.ADD) {
            YearSummary yearSummary = summary.getYears().stream()
                    .filter(ys -> ys.getYear() == workload.getYear())
                    .findFirst()
                    .orElseGet(() -> {
                        YearSummary ys = new YearSummary();
                        ys.setYear(workload.getYear());
                        ys.setMonths(new ArrayList<>());
                        summary.getYears().add(ys);
                        return ys;
                    });

            MonthSummary monthSummary = yearSummary.getMonths().stream()
                    .filter(ms -> ms.getMonth() == workload.getMonth())
                    .findFirst()
                    .orElseGet(() -> {
                        MonthSummary ms = new MonthSummary();
                        ms.setMonth(workload.getMonth());
                        ms.setDuration(0);
                        yearSummary.getMonths().add(ms);
                        return ms;
                    });

            monthSummary.setDuration(monthSummary.getDuration() + workload.getTrainingDuration());
        }
    }


    public TrainerSummaryByMonth getTrainerSummaryByMonthAndYear(String username, int year, int month) {
        Trainer trainer = trainerRepository.findByUsername(username).orElseThrow(TrainerNotFoundException::new);
        List<Workload> workloads = workloadRepository.findByTrainerOrderByYearAsc(trainer)
                .stream().filter(workload -> workload.getYear() == year && workload.getActionType() == ActionType.ADD)
                .toList();
        int total = 0;
        for (Workload workload : workloads) {
            if (workload.getMonth() == month) {
                total = total + workload.getTrainingDuration();
            }
        }
        return new TrainerSummaryByMonth(month, total);
    }
}
