package com.example.project.domain.entities;

import com.example.project.rest.dto.UserRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String contactNumber;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Reserves> reserves = new ArrayList<>();

    public static Users of(UserRequestDto dto){
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setContactNumber(dto.getContactNumber());
        user.setCpf(dto.getCpf());
        user.setPassword(dto.getPassword());
        return user;
    }
}
