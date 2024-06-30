package com.example.project.domain.entities;

import com.example.project.rest.dto.MovieRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class Movies implements Serializable {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(length = 10000)
    private String description;
    private LocalDate releaseData;

    private String imageUrl;

    @OneToMany(mappedBy = "movie")
    private List<Reviews> reviews = new ArrayList<>();

    public static Movies of(MovieRequestDto dto){
        var movie = new Movies();
        movie.setName(dto.getName());
        movie.setDescription(dto.getDescription());
        movie.setReleaseData(LocalDate.parse(dto.getReleaseData(), formatter));
        movie.setImageUrl(dto.getImageUrl());
        return movie;
    }

    public Double getAverageRating(){
        if(reviews.isEmpty()){
            return 0.0;
        }
        int totalRating = 0;
        for(Reviews review : reviews){
            totalRating += review.getRating();
        }

        return (double) totalRating / reviews.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movies movies = (Movies) o;
        return Objects.equals(id, movies.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
