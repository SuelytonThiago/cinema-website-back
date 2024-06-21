package com.example.project.domain.repositories;

import com.example.project.domain.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Reviews,Long> {
}
