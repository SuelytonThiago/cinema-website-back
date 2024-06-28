package com.example.project.domain.entities;

import com.example.project.rest.dto.ReviewRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "reviews")
public class Reviews implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @Min(1)
    @Max(5)
    private Integer rating;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movieId")
    private Movies movie;

    public static Reviews of(ReviewRequestDto dto,Users user, Movies movie){
        var reviews = new Reviews();
        reviews.setComment(dto.getComment());
        reviews.setDate(LocalDate.now());
        reviews.setRating(dto.getRating());
        reviews.setUser(user);
        reviews.setMovie(movie);
        return reviews;
    }

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
