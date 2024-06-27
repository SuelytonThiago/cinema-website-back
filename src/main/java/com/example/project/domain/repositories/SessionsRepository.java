package com.example.project.domain.repositories;

import com.example.project.domain.entities.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionsRepository extends JpaRepository<Sessions,Long> {
}
