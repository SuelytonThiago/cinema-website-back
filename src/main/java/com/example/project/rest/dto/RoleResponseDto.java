package com.example.project.rest.dto;

import com.example.project.domain.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleResponseDto {

    private String nameRole;

    public static RoleResponseDto of(Roles roles){
        var response = new RoleResponseDto();
        response.setNameRole(roles.getRoleName());
        return response;
    }
}
