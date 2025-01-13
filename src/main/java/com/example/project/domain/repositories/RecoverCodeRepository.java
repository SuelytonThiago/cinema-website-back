package com.example.project.domain.repositories;

import com.example.project.domain.entities.RecoverCode;
import com.example.project.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoverCodeRepository extends JpaRepository<RecoverCode, Long> {

    Optional<RecoverCode> findByCodeAndUser(String value, Users user);
}
