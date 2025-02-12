package com.example.project.rest.services;

import com.example.project.domain.entities.Roles;
import com.example.project.domain.repositories.RoleRepository;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    public Roles findByName(String name){
        return roleRepository.findByRoleName(name)
                .orElseThrow(() -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("role.service.error.notFound", null, LocaleContextHolder.getLocale())
                ));
    }
}
