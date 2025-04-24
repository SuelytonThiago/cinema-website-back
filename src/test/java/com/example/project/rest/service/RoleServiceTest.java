package com.example.project.rest.service;

import com.example.project.domain.entities.Roles;
import com.example.project.domain.repositories.RoleRepository;
import com.example.project.rest.services.RoleService;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.MessageSource;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private RoleService roleService;

    private Roles role;

    @BeforeEach
    void setUp() {

        role = new Roles(1L, "ROLE_ADMIN");

    }


    @Test
    void testFindByName() {
        given(roleRepository.findByRoleName(anyString())).willReturn(Optional.of(role));

        var response = roleService.findByName(role.getRoleName());

        assertThat(response).isEqualTo(role);
        verify(roleRepository).findByRoleName(anyString());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void testFindByNameWithObjectNotFoundError() {
        given(roleRepository.findByRoleName(anyString())).willReturn(Optional.empty());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> roleService.findByName(role.getRoleName()))
                .withMessage(
                        messageSource.getMessage("role.service.error.notFound", null, LocaleContextHolder.getLocale())
                );

    }


}
