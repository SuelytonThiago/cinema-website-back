package com.example.project.rest.controllers;

import com.example.project.rest.dto.UserRequestDto;
import com.example.project.rest.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UsersService  usersService;

    @PostMapping("/create")
    public ResponseEntity<Void> createNewUser(@RequestBody @Valid UserRequestDto dto){
        usersService.createNewUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UserRequestDto dto,
                                           HttpServletRequest request){
        usersService.updateUserData(dto,request);
        return ResponseEntity.noContent().build();
    }

}

