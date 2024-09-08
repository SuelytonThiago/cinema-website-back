package com.example.project.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChairResponseDto {

    private Integer chairNumber;
    private boolean available;

}
