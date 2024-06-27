package com.example.project.domain.entities;


import com.example.project.rest.dto.SessionRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sessions")
public class Sessions {

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

    @OneToMany(mappedBy = "sessions" )
    private List<Chairs> chairs = new ArrayList<>();

    public static Sessions of(SessionRequestDto dto, Movies movie){
        var room = new Sessions();
        room.setName(dto.getName());
        room.setMovie(movie);
        room.setDateStart(convertStringToLocalDateTime(dto.getDateStart()));
        room.setDateEnd(convertStringToLocalDateTime(dto.getDateEnd()));
        return room;
    }

    public static LocalDateTime convertStringToLocalDateTime(String data){
        return LocalDateTime.parse(data,formatter);
    }
}
