package com.example.project.rest.services;

import com.example.project.domain.entities.RecoverCode;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.RecoverCodeRepository;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.dto.EmailDto;
import com.example.project.rest.services.exceptions.CustomException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class RecoverCodeService {

    private static final String TEMPLATE_FILE_USER = "src/main/resources/templates/sending-email-user.html";

    private static final String TEMPLATE_FILE_ANONYMOUS = "src/main/resources/templates/sending-email-anonymous.html";


    private final RecoverCodeRepository recoverCodeRepository;
    private static final int CODE_LENGTH = 6;
    private static final Random random = new Random();
    private final UsersRepository usersRepository;
    private final RabbitMQService rabbitMQService;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    @Transactional
    public String generateRecoverCode(Users user){
        var recoverCode = new RecoverCode();

        var code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }

        recoverCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        recoverCode.setCode(code.toString());
        recoverCode.setUser(user);
        recoverCodeRepository.save(recoverCode);

        return code.toString();
    }

    public String validateCode(String codeValue, String email){


        var user = usersRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(
                        messageSource.getMessage("recover.code.error.invalid", null, LocaleContextHolder.getLocale())
                )
        );

        var code = recoverCodeRepository.findByCodeAndUser(codeValue, user).orElseThrow(
                () -> new CustomException(
                        messageSource.getMessage("recover.code.error.invalid", null, LocaleContextHolder.getLocale())
                )
        );

        if(!code.isValid()){
            throw new CustomException(
                    messageSource.getMessage("recover.code.error.expired", null, LocaleContextHolder.getLocale())
            );
        }

        return jwtService.generateAccessToken(user);
    }

    @Transactional
    public String recoverPassword(String email) {
        try{
            var userOptional = usersRepository.findByEmail(email);

            if(userOptional.isPresent()){
                var user = userOptional.get();
                var code = generateRecoverCode(user);

                var htmlContent = new String(Files.readAllBytes(Paths.get(TEMPLATE_FILE_USER)));

                htmlContent = htmlContent.replace("{{ownerRef}}",user.getName());
                htmlContent = htmlContent.replace("{{code}}",code);

                var message = new EmailDto();
                message.setHtmlContent(htmlContent);
                message.setEmailTo(email);
                message.setSubject("solicitação de alteração de senha do usuário ");
                message.setEmailFrom("cinemaxsup@gmail.com");

                rabbitMQService.sendEmailMessage(message);

                return email;
            }

            var htmlContent = new String(Files.readAllBytes(Paths.get(TEMPLATE_FILE_ANONYMOUS)));

            htmlContent = htmlContent.replace("{{email}}", email);

            var message = new EmailDto();
            message.setHtmlContent(htmlContent);
            message.setEmailTo(email);
            message.setSubject("solicitação de alteração de senha do usuário ");
            message.setEmailFrom("cinemaxsup@gmail.com");

            rabbitMQService.sendEmailMessage(message);

            return email;

        } catch(IOException e){

            throw new CustomException(
                    messageSource.getMessage("recoverCode.service.error", null, LocaleContextHolder.getLocale())
            );
        }
    }
}
