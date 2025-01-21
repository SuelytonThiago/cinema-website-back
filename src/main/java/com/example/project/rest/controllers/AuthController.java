package com.example.project.rest.controllers;

import com.example.project.rest.dto.UserLoginDto;
import com.example.project.rest.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    @Operation(summary = "do user authentication")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid UserLoginDto dto){
        return ResponseEntity.ok(service.generateTokens(dto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "update access token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request){
        return ResponseEntity.ok(service.attAccessToken(request));
    }
}
