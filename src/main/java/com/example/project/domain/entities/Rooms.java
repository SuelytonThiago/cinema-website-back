package com.example.project.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "rooms")
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "room" )
    private List<Chairs> chairs = new ArrayList<>();

    @OneToMany(mappedBy = "reserveRoom" )
    private List<Reserves> reserves = new ArrayList<>();

    @OneToMany(mappedBy = "sessionRoom")
    private List<Sessions> sessions = new ArrayList<>();

    private Integer maxSeats = 40;
}
