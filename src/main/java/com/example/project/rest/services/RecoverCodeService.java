package com.example.project.rest.services;

import com.example.project.domain.entities.RecoverCode;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.RecoverCodeRepository;
import com.example.project.rest.dto.EmailDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class RecoverCodeService {

    private final RecoverCodeRepository recoverCodeRepository;
    private static final int CODE_LENGTH = 6;
    private static final Random random = new Random();
    private final UsersService usersService;
    private final RabbitMQService rabbitMQService;

    @Transactional
    public String generateRecoverCode(Users user){
        var recoverCode = new RecoverCode();

        var code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }

        recoverCode.setCode(code.toString());
        recoverCode.setUser(user);
        recoverCodeRepository.save(recoverCode);

        return code.toString();
    }

    public void recoverPassword(EmailDto dto){
        rabbitMQService.sendEmailMessage(dto);
    }
}
