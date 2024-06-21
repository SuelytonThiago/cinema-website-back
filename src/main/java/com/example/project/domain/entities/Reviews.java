package com.example.project.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Double rating;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movieId")
    private Movies movieId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reviews reviews = (Reviews) o;
        return Objects.equals(id, reviews.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
