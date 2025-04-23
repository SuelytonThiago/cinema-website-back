package com.example.project.rest.service;

import com.example.project.domain.entities.RecoverCode;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.RecoverCodeRepository;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.dto.EmailDto;
import com.example.project.rest.services.JwtService;
import com.example.project.rest.services.RabbitMQService;
import com.example.project.rest.services.RecoverCodeService;
import com.example.project.rest.services.exceptions.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RecoverCodeServiceTest {

    @Mock
    private RecoverCodeRepository recoverCodeRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RabbitMQService rabbitMQService;
    @Mock
    private JwtService jwtService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private RecoverCodeService recoverCodeService;

    private Users user;
    private String accessToken;
    private RecoverCode recoverCode;


    @BeforeEach
    void setUp() {
        user = new Users();

        user.setName("maria");
        user.setEmail("maria@example.com");
        user.setContactNumber("99940028922");
        user.setCpf("87466407030");
        user.setPassword("Senha123");
        user.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.jpg");

        accessToken = "token";

        recoverCode = new RecoverCode();
        recoverCode.setCode("123456");
        recoverCode.setUser(user);
        recoverCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        recoverCode.setId(1L);

    }

    @Test
    void testGenerateRecoverCode() {
        var code = recoverCodeService.generateRecoverCode(user);

        assertThat(code.split("").length).isEqualTo(6);
        verify(recoverCodeRepository).save(any(RecoverCode.class));
        verifyNoMoreInteractions(recoverCodeRepository);
    }

    @Test
    void testValidateCode() {
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(recoverCodeRepository.findByCodeAndUser(anyString(),any(Users.class))).willReturn(Optional.of(recoverCode));
        given(jwtService.generateAccessToken(any(Users.class))).willReturn(accessToken);

        var response = recoverCodeService.validateCode(recoverCode.getCode(),user.getEmail());

        assertThat(response).isNotNull();

        assertThat(response).isEqualTo(accessToken);
        verify(usersRepository).findByEmail(anyString());
        verify(recoverCodeRepository).findByCodeAndUser(anyString(),any(Users.class));
        verify(jwtService).generateAccessToken(any(Users.class));
        verifyNoMoreInteractions(usersRepository);
        verifyNoMoreInteractions(recoverCodeRepository);
        verifyNoMoreInteractions(jwtService);

    }

    @Test
    void testValidateCodeIfInvalidEmail() {
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> recoverCodeService.validateCode(recoverCode.getCode(),user.getEmail()))
                .withMessage(
                        messageSource.getMessage("recover.code.error.invalid", null, LocaleContextHolder.getLocale())
                );

        verify(usersRepository).findByEmail(anyString());
        verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testValidateCodeIfInvalidCode() {
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(recoverCodeRepository.findByCodeAndUser(anyString(),any(Users.class))).willReturn(Optional.empty());

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> recoverCodeService.validateCode(recoverCode.getCode(),user.getEmail()))
                .withMessage(
                        messageSource.getMessage("recover.code.error.invalid", null, LocaleContextHolder.getLocale())
                );

        verify(usersRepository).findByEmail(anyString());
        verify(recoverCodeRepository).findByCodeAndUser(anyString(),any(Users.class));
        verifyNoMoreInteractions(recoverCodeRepository);
        verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void testValidateCodeIfExpiredCode() {
        recoverCode.setExpiresAt(LocalDateTime.now().minusMinutes(10));

        given(usersRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(recoverCodeRepository.findByCodeAndUser(anyString(),any(Users.class))).willReturn(Optional.of(recoverCode));

        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> recoverCodeService.validateCode(recoverCode.getCode(),user.getEmail()))
                .withMessage(
                        messageSource.getMessage("recover.code.error.invalid", null, LocaleContextHolder.getLocale())
                );


        verify(usersRepository).findByEmail(anyString());
        verify(recoverCodeRepository).findByCodeAndUser(anyString(),any(Users.class));
        verifyNoMoreInteractions(usersRepository);
        verifyNoMoreInteractions(recoverCodeRepository);

    }

    @Test
    void testRecoverPasswordWithRegisteredUser() {
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        recoverCodeService.recoverPassword(user.getEmail());

        verify(usersRepository).findByEmail(anyString());
        verify(rabbitMQService).sendEmailMessage(any(EmailDto.class));
        verifyNoMoreInteractions(usersRepository);
        verifyNoMoreInteractions(rabbitMQService);

    }

    @Test
    void testRecoverPasswordWithAnonymousUser () {
        given(usersRepository.findByEmail(anyString())).willReturn(Optional.empty());

        recoverCodeService.recoverPassword(user.getEmail());

        verify(usersRepository).findByEmail(anyString());
        verify(rabbitMQService).sendEmailMessage(any(EmailDto.class));
        verifyNoMoreInteractions(usersRepository);
        verifyNoMoreInteractions(rabbitMQService);

    }
}
