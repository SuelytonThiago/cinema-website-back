package com.example.project.rest.dto;

import com.example.project.domain.entities.Categories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryResponseDto {

    private String name;

    public static CategoryResponseDto of(Categories categories){
        var response =  new CategoryResponseDto();
        response.setName(categories.getName());
        return response;
    }
}
