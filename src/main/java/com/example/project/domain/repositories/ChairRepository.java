package com.example.project.domain.repositories;

import com.example.project.domain.entities.Chairs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChairRepository extends JpaRepository<Chairs,Long> {
}
