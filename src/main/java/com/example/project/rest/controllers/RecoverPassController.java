package com.example.project.rest.controllers;

import com.example.project.rest.services.RecoverCodeService;
import com.example.project.rest.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class RecoverPassController {

    @Autowired
    private RecoverCodeService recoverCodeService;

    @Autowired
    private UsersService usersService;


    @PostMapping("/recover-password")
    @Operation(summary = "send an email with a code to recover your password")
    public ResponseEntity<String> recoverPassword(@RequestParam @Email String email){
        return ResponseEntity.ok(recoverCodeService.recoverPassword(email));
    }

    @PostMapping("/verify-code")
    @Operation(summary = "check if the code is correct")
    public ResponseEntity<String> verifyCode(@RequestParam String code, @RequestParam String email){
        return ResponseEntity.ok(recoverCodeService.validateCode(code, email));
    }

}
