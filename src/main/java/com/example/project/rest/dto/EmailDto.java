package com.example.project.rest.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    private String emailTo;
    private String htmlContent;
    private String emailFrom;
    private String subject;
}
