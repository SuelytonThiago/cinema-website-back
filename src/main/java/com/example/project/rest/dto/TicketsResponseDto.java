package com.example.project.rest.dto;

import com.example.project.domain.entities.Chairs;
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
    private String chairName;

    public static TicketsResponseDto of(Tickets ticket){
        var response = new TicketsResponseDto();
        response.setChairName(ticket.getChair().getName());
        response.setMovieName(ticket.getSession().getMovie().getName());
        response.setSessionName(ticket.getSession().getName());
        response.setPrice(ticket.getPrice());
        return response;
    }
}
