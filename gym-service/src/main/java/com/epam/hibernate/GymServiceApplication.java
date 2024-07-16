package com.epam.hibernate;


import com.epam.hibernate.repository.TrainingTypeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GymServiceApplication {

    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(GymServiceApplication.class, args);

        TrainingTypeRepository trainingTypeRepository = run.getBean(TrainingTypeRepository.class);
        trainingTypeRepository.addTrainingTypes();
    }

}
