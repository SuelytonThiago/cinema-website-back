package com.example.project.rest.controllers;

import com.example.project.rest.dto.EmailDto;
import com.example.project.rest.services.RecoverCodeService;
import com.example.project.rest.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class RecoverPassController {

    @Autowired
    private RecoverCodeService recoverCodeService;

    @Autowired
    private UsersService usersService;


    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam String email){
        return ResponseEntity.ok(recoverCodeService.recoverPassword(email));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestParam String code, HttpServletRequest request){
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);

        recoverCodeService.validateCode(code, user);
        return ResponseEntity.ok().build();
    }

}
