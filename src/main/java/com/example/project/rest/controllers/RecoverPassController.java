package com.example.project.rest.controllers;

import com.example.project.rest.services.RecoverCodeService;
import com.example.project.rest.services.UsersService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecoverPassController {

    @Autowired
    private RecoverCodeService recoverCodeService;

    @Autowired
    private UsersService usersService;


    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam @Email String email){
        return ResponseEntity.ok(recoverCodeService.recoverPassword(email));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String code, @RequestParam String email){
        return ResponseEntity.ok(recoverCodeService.validateCode(code, email));
    }

}
