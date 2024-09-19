package com.example.project.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketRequestDto {

    @NotNull(message = "this session id cannot be null")
    private Long sessionId;
    private Double price;
    private Integer chairNumber;
    private String type;
}
