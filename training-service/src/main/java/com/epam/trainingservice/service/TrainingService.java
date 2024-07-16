package com.epam.trainingservice.service;

import com.epam.trainingservice.entity.Trainer;
import com.epam.trainingservice.entity.Workload;
import com.epam.trainingservice.entity.enums.ActionType;
import com.epam.trainingservice.exception.WorkloadNotFoundException;
import com.epam.trainingservice.repository.TrainerRepository;
import com.epam.trainingservice.repository.WorkloadRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.epam.trainingservice.utils.DateUtils.*;
@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);
    private final TrainerRepository trainerRepository;
    private final WorkloadRepository workloadRepository;

    public TrainingService(TrainerRepository trainerRepository, WorkloadRepository workloadRepository) {
        this.trainerRepository = trainerRepository;
        this.workloadRepository = workloadRepository;
    }

    @Transactional
    public boolean updateWorkload(String username, String firstName, String lastName,
                                  Boolean status, Integer duration, Date trainingDate, ActionType action) {
        try {
            if (Arrays.stream(ActionType.values()).noneMatch(action::equals)) {
                throw new IllegalArgumentException("Wrong Action Type");
            }

            Trainer trainer = findAndSaveTrainer(username, firstName, lastName, status, duration, trainingDate, action);

            trainerRepository.save(trainer);
            if (duration > 0) {
                workloadRepository.save(new Workload(getYear(trainingDate), getMonth(trainingDate), getDay(trainingDate), duration, action, trainer));
            }
            return true;
        } catch (Exception e) {
            log.error("Exception occurred during updating workload: " + e);
            return false;
        }
    }

    private Trainer findAndSaveTrainer(String username, String firstName, String lastName,
                                       Boolean status, Integer duration, Date trainingDate, ActionType actionType) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        if (trainer.isEmpty()) {
            trainer = Optional.of(new Trainer(username, firstName, lastName, status, duration));
            trainerRepository.save(trainer.get());

        } else {
            if (ActionType.ADD.equals(actionType)) {
                trainer.get().setTotalHours(trainer.get().getTotalHours() + duration);
            } else {
                trainer.get().setTotalHours(trainer.get().getTotalHours() - duration);
                Optional<Workload> workload = workloadRepository.findFirstByDayAndMonthAndYearAndTrainerAndTrainingDuration(getDay(trainingDate), getMonth(trainingDate), getYear(trainingDate), trainer.get(), duration);

                Workload current = workload.orElseThrow(WorkloadNotFoundException::new);
                current.setTrainingDuration(0);
                current.setActionType(ActionType.CANCEL);
                workloadRepository.save(current);
            }
        }
        return trainer.get();
    }
}
