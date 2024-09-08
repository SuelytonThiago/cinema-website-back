package com.example.project.rest.dto;

import com.example.project.domain.entities.Tickets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketsResponseDto {

    private String movieName;
    private String sessionName;
    private Double price;
    private Integer chairNumber;

    public static TicketsResponseDto of(Tickets ticket){
        var response = new TicketsResponseDto();
        response.setChairNumber(ticket.getChair().getNumber());
        response.setMovieName(ticket.getSession().getMovie().getName());
        response.setSessionName(ticket.getSession().getName());
        response.setPrice(ticket.getPrice());
        return response;
    }
}
