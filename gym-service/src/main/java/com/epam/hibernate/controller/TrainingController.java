package com.epam.hibernate.controller;

import com.epam.hibernate.config.LogEntryExit;
import com.epam.hibernate.dto.AddTrainingRequest;
import com.epam.hibernate.entity.TrainingType;
import com.epam.hibernate.service.TrainingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.NotActiveException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/admin/training", consumes = {"application/JSON"}, produces = {"application/JSON", "application/XML"})
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TrainingController {
    private final TrainingService trainingService;
    Counter addCounter;

    public TrainingController(TrainingService trainingService, MeterRegistry meterRegistry) {
        this.trainingService = trainingService;
        this.addCounter = Counter.builder("training_add_counter")
                .description("Number of Hits on Adding Training")
                .register(meterRegistry);
    }

    @PostMapping(value = "/add")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Add Training", description = "This method adds new  Training")
    @CircuitBreaker(name = "addTrainingCircuitBreaker")
    public ResponseEntity<?> addTraining(@RequestBody AddTrainingRequest request) throws NotActiveException {
        addCounter.increment();
        return trainingService.addTraining(request);
    }

    @DeleteMapping(value = "/delete/{trainingId}")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Remove Training", description = "This method removes Training")
    @CircuitBreaker(name = "removeTrainingCircuitBreaker")
    public ResponseEntity<?> removeTraining(@PathVariable Long trainingId) {
        return trainingService.removeTraining(trainingId);
    }

    @GetMapping(value = "/types")
    @LogEntryExit(showArgs = true, showResult = true)
    @Operation(summary = "Get Training Types", description = "This method returns Training Types")
    @CircuitBreaker(name = "getTrainingTypesCircuitBreaker")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        return trainingService.getTrainingTypes();
    }

}
