package com.example.project.domain.repositories;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categories, Long> {

    @Query("SELECT p FROM Categories p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Categories> findByNameLike(@Param("name") String name);

    Optional<Categories> findByName(String name);
}
