package com.example.project.rest.services;

import com.example.project.domain.entities.Roles;
import com.example.project.domain.repositories.RoleRepository;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Roles findByName(String name){
        return roleRepository.findByRoleName(name)
                .orElseThrow(() -> new ObjectNotFoundExceptions("this role is not found"));
    }
}
