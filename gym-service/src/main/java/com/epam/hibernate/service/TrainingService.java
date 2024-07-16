package com.epam.hibernate.service;

import com.epam.hibernate.dto.ActionType;
import com.epam.hibernate.dto.AddTrainingRequest;
import com.epam.hibernate.dto.TrainingInfoMessage;
import com.epam.hibernate.entity.Trainee;
import com.epam.hibernate.entity.Trainer;
import com.epam.hibernate.entity.Training;
import com.epam.hibernate.entity.TrainingType;
import com.epam.hibernate.exception.TrainingNotFoundException;
import com.epam.hibernate.repository.TraineeRepository;
import com.epam.hibernate.repository.TrainerRepository;
import com.epam.hibernate.repository.TrainingRepository;
import com.epam.hibernate.repository.TrainingTypeRepository;
import com.epam.hibernate.service.jms.TrainingInfoSender;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.util.List;

@Service
public class TrainingService {
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingInfoSender trainingInfoSender;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingService(TrainerRepository trainerRepository, TraineeRepository traineeRepository, TrainingTypeRepository trainingTypeRepository, TrainingInfoSender trainingInfoSender, TrainingRepository trainingRepository) {
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingInfoSender = trainingInfoSender;
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public ResponseEntity<?> addTraining(@NotNull AddTrainingRequest request) throws NotActiveException {
        Trainee trainee = traineeRepository.selectByUsername(request.getTraineeUsername());
        Trainer trainer = trainerRepository.selectByUsername(request.getTrainerUsername());
        if (!trainer.getUser().getActive() || !trainee.getUser().getActive()) {
            throw new NotActiveException("Trainer/Trainee is not active");
        }

        TrainingType trainingType = trainingTypeRepository.selectByType(request.getTrainingType());
        if (trainer.getSpecialization().getTrainingTypeName() != trainingType.getTrainingTypeName()) {
            throw new IllegalArgumentException("Trainer has not that specialization");
        }

        Training training = new Training(trainer, trainee, request.getTrainingName(), trainingType, request.getTrainingDate(), request.getDuration());

        trainer.getTrainings().add(training);
        trainer.getTrainees().add(trainee);

        trainee.getTrainings().add(training);
        trainee.getTrainers().add(trainer);

        trainerRepository.save(trainer);
        traineeRepository.save(trainee);

        trainingInfoSender.send(new TrainingInfoMessage(training.getTrainer().getUser().getUsername(),
                training.getTrainer().getUser().getFirstName(),
                training.getTrainer().getUser().getLastName(), training.getTrainer().getUser().getActive(),
                training.getTrainingDate(), training.getTrainingDuration(),
                ActionType.ADD));
        return ResponseEntity.status(200).body("Training added successfully");
    }

    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        return ResponseEntity.ok().body(trainingTypeRepository.getAll());
    }

    public ResponseEntity<?> removeTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId);
        if (training == null) {
            throw new TrainingNotFoundException();
        }
        trainingRepository.delete(trainingId);
        trainingInfoSender.send(new TrainingInfoMessage(training.getTrainer().getUser().getUsername(),
                training.getTrainer().getUser().getFirstName(),
                training.getTrainer().getUser().getLastName(),
                training.getTrainer().getUser().getActive(), training.getTrainingDate(),
                training.getTrainingDuration(), ActionType.CANCEL));
        return ResponseEntity.ok("Training removed successfully");
    }
}
