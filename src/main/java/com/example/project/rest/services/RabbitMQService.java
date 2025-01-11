package com.example.project.rest.services;

import com.example.project.config.RabbitConfig;
import com.example.project.rest.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailMessage(EmailDto emailDto) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_MS_EMAIL, emailDto);
    }
}
