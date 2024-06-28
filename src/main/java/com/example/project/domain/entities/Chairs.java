package com.example.project.domain.entities;

import com.example.project.domain.enums.ChairStatus;
import com.example.project.rest.dto.ChairRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "chairs")
public class Chairs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @JsonIgnore
    @OneToOne(mappedBy = "chair")
    private Tickets ticket;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="sessionId")
    private Sessions sessions;

    public static Chairs of(ChairRequestDto dto,Sessions sessions,Users user){
        var chair = new Chairs();
        chair.setSessions(sessions);
        chair.setUser(user);
        chair.setName(dto.getName());
        return chair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chairs chairs = (Chairs) o;
        return Objects.equals(id, chairs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
