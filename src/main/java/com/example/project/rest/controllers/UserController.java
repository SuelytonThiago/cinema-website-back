package com.example.project.rest.controllers;

import com.example.project.rest.dto.UserRequestDto;
import com.example.project.rest.dto.UserResponseDto;
import com.example.project.rest.services.UsersService;
import com.example.project.rest.services.validations.Password;
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

    @GetMapping
    public ResponseEntity<UserResponseDto> findById(HttpServletRequest request) {
        return ResponseEntity.ok(usersService.findUserById(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> createNewUser(@RequestParam @Password String password, HttpServletRequest request){
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);
        usersService.changePassword(user, password);
        return ResponseEntity.noContent().build();
    }


}

