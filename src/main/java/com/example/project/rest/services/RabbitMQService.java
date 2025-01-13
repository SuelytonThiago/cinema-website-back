package com.example.project.rest.services;

import com.example.project.rest.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    public static final String QUEUE_MS_EMAIL = "MS_EMAIL";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailMessage(EmailDto emailDto) {
        rabbitTemplate.convertAndSend(QUEUE_MS_EMAIL, emailDto);
    }
}
