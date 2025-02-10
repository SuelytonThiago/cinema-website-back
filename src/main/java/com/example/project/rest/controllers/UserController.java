package com.example.project.rest.controllers;

import com.example.project.rest.dto.UserRequestDto;
import com.example.project.rest.dto.UserResponseDto;
import com.example.project.rest.dto.UserUpdateRequestDto;
import com.example.project.rest.services.UsersService;
import com.example.project.rest.services.validations.Password;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UsersService  usersService;

    @GetMapping
    public ResponseEntity<UserResponseDto> findById(HttpServletRequest request) {
        return ResponseEntity.ok(usersService.findUserById(request));
    }

    @PostMapping("/change-password")
    @Operation(summary = "change user password")
    public ResponseEntity<Void> changePassword(@RequestParam @Password String password, HttpServletRequest request){
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);
        usersService.changePassword(user, password);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    @Operation(summary = "create a new account")
    public ResponseEntity<Void> createNewUser(@RequestBody @Valid UserRequestDto dto){
        usersService.createNewUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/update")
    @Operation(summary = "update user data")
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UserUpdateRequestDto dto,
                                               @RequestParam String password,
                                               HttpServletRequest request){
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);

        usersService.updateUserData(dto,user, password);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-password")
    @Operation(summary = "change user password")
    public ResponseEntity<Void> updateUserPassword(@RequestParam String oldPassword,
                                                   @RequestParam String newPassword,
                                                   HttpServletRequest request){
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);

        usersService.createNewPassword(oldPassword,newPassword,user);
        return ResponseEntity.noContent().build();
    }




}

