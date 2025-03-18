package com.example.project.rest.dto;

import com.example.project.domain.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String contactNumber;
    private String password;
    private String profileImg;
    private List<RoleResponseDto> roles = new ArrayList<>();

    public static UserResponseDto of(Users user) {
        var response = new UserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setCpf(user.getCpf());
        response.setEmail(user.getEmail());
        response.setContactNumber(user.getContactNumber());
        response.setProfileImg(user.getProfileImg());
        response.roles.addAll(user.getRoles().stream().map(RoleResponseDto::of).toList());
        return response;
    }
}
