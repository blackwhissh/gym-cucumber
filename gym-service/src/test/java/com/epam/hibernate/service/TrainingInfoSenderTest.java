package com.epam.hibernate.service;

import com.epam.hibernate.dto.ActionType;
import com.epam.hibernate.dto.TrainingInfoMessage;
import com.epam.hibernate.service.jms.TrainingInfoSender;
import jakarta.jms.Destination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrainingInfoSenderTest {
    @Mock
    private JmsTemplate jmsTemplate;
    @InjectMocks
    private TrainingInfoSender trainingInfoSender;

    @Test
    public void testSend() {
        TrainingInfoMessage request = new TrainingInfoMessage(
                "test", "test", "test", true, Date.from(Instant.now()), 4, ActionType.ADD
        );

        ReflectionTestUtils.setField(trainingInfoSender, "TRAINING_INFO_QUEUE", "testQueue");
        trainingInfoSender.send(request);

        verify(jmsTemplate, times(1)).convertAndSend(any(String.class), eq(request));
    }
}
