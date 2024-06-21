package com.example.project.domain.repositories;

import com.example.project.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findFirstByEmailOrCpf(String email, String cpf);
}
