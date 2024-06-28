package com.example.project.domain.repositories;

import com.example.project.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles,Long> {

    Optional<Roles> findByRoleName(String roleName);
}
