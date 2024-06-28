package com.example.project.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChairRequestDto {

    @NotBlank(message = "this name cannot be empty or null")
    public String name;

    @NotNull(message = "this session id cannot be null")
    public Long sessionId;

}
