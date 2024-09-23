package com.example.project.domain.repositories;

import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Reviews;
import com.example.project.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewsRepository extends JpaRepository<Reviews,Long> {

    Optional<Reviews> findByUser(Users user);
    Optional<Reviews> findByUserAndMovie(Users user , Movies movie);

}
