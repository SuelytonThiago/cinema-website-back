package com.example.project.domain.repositories;

import com.example.project.domain.entities.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionsRepository extends JpaRepository<Sessions,Long> {

    @Query("SELECT e FROM Sessions e WHERE e.dateStart > :currentDate")
    List<Sessions> findAllWithDateAfter(@Param("currentDate") LocalDateTime currentDate);
}
