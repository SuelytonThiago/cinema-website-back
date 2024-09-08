package com.example.project.domain.entities;


import com.example.project.rest.dto.ChairResponseDto;
import com.example.project.rest.dto.SessionRequestDto;
import com.example.project.rest.services.exceptions.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sessions")
public class Sessions implements Serializable {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;

    @ManyToOne
    @JoinColumn(name = "movieId")
    private Movies movie;

    @OneToMany(mappedBy = "session" )
    private List<Chairs> chairs = new ArrayList<>();

    private boolean[] chairsAvailable = new boolean[40];

    public static Sessions of(SessionRequestDto dto, Movies movie){
        var room = new Sessions();
        room.setName(dto.getName());
        room.setMovie(movie);
        room.setDateStart(convertStringToLocalDateTime(dto.getDateStart()));
        room.setDateEnd(convertStringToLocalDateTime(dto.getDateEnd()));
        return room;
    }


    public static LocalDateTime convertStringToLocalDateTime(String data){
        if (data == null || data.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(data,formatter);
    }

    public boolean isChairAvailable(int index) {
        return !chairsAvailable[index];
    }

    public void reserveChair(int index) {
        if (index < 0 || index >= chairsAvailable.length) {
            throw new CustomException("Invalid chair number");
        }
        if (!chairsAvailable[index]) {
            chairsAvailable[index] = true;
        } else {
            throw new CustomException("Chair is already occupied");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sessions sessions = (Sessions) o;
        return Objects.equals(id, sessions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
