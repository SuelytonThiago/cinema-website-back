package com.example.project.domain.repositories;

import com.example.project.domain.entities.Tickets;
import com.example.project.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketsRepository extends JpaRepository<Tickets,Long> {

    List<Tickets> findByUser(Users user);
}
