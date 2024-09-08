package com.example.project.domain.entities;

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

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="sessionId")
    private Sessions session;

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
