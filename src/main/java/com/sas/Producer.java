package com.sas;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.config.rabbitmq.exchange}")
    private String topicExchangeName;

    @Value("${app.config.rabbitmq.routing.key}")
    private String routingKey;

    public void sendMessage() {

        String message = generateMessage();
        log.info("Sending message ... {}", message);
        Object response = rabbitTemplate.convertSendAndReceive(
                topicExchangeName,
                routingKey,
                message
        );
        log.info("Received message ... {}", response);
    }

    private String generateMessage() {
        return RandomStringUtils.randomAlphabetic(3, 12).toLowerCase();
    }

}