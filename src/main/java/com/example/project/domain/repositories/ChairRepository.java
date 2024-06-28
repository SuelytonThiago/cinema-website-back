package com.example.project.domain.repositories;

import com.example.project.domain.entities.Chairs;
import com.example.project.domain.entities.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChairRepository extends JpaRepository<Chairs,Long> {

    Optional<Chairs> findByNameAndSession(String name, Sessions sessions);
}
