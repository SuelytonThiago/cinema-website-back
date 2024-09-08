package com.example.project.domain.entities;

import com.example.project.rest.dto.TicketRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ticket")
public class Tickets implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessionId")
    private Sessions session;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    private Double price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chairId")
    private Chairs chair;

    private boolean expired;

    public static Tickets of(TicketRequestDto dto, Users user, Sessions session, Chairs chair){
        var ticket = new Tickets();
        ticket.setUser(user);
        ticket.setSession(session);
        ticket.setExpired(false);
        ticket.setChair(chair);
        ticket.setPrice(dto.getPrice());
        return ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tickets tickets = (Tickets) o;
        return Objects.equals(id, tickets.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
