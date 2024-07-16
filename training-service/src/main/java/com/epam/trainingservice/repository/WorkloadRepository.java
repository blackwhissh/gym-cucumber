package com.epam.trainingservice.repository;

import com.epam.trainingservice.entity.Trainer;
import com.epam.trainingservice.entity.Workload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WorkloadRepository extends JpaRepository<Workload, Long> {
    List<Workload> findByTrainerOrderByYearAsc(Trainer trainer);
    Optional<Workload> findFirstByDayAndMonthAndYearAndTrainerAndTrainingDuration(int day, int month, int year, Trainer trainer, int duration);
    void deleteByYearAndMonthAndDayAndTrainer(int year, int month, int day, Trainer trainer);
}
