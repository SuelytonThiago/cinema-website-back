package com.example.project.domain.entities;

import com.example.project.rest.dto.TicketRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ticket")
public class Tickets {

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

    public static Tickets of(Users user, Sessions session){
        var ticket = new Tickets();
        ticket.setUser(user);
        ticket.setSession(session);
        ticket.setExpired(false);
        return ticket;
    }
}
