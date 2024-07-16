package com.epam.hibernate.service.jms;

import com.epam.hibernate.dto.TrainingInfoMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainingInfoSender {
    @Value("${spring.activemq.queue}")
    private String TRAINING_INFO_QUEUE;
    private final JmsTemplate jmsTemplate;

    public TrainingInfoSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void send(TrainingInfoMessage request) {
        jmsTemplate.convertAndSend(TRAINING_INFO_QUEUE, request);
    }
}
