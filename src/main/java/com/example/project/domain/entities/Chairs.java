package com.example.project.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "chairs")
public class Chairs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Users user;
    private Integer chairNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="roomId")
    private Rooms room;
}
