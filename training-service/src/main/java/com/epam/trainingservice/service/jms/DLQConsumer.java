package com.epam.trainingservice.service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class DLQConsumer {
    public static final Logger log = LoggerFactory.getLogger(DLQConsumer.class);

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void receive(Message<Object> message){
        log.info("Received message from DLQ: " + message.getPayload());
    }
}
