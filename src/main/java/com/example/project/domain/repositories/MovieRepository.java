package com.example.project.domain.repositories;

import com.example.project.domain.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movies, Long> {

    @Query("SELECT p FROM Movies p WHERE p.name LIKE %:name%")
    List<Movies> findByNameLike(@Param("name") String name);
}
