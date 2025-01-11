package com.example.project.rest.controllers;

import com.example.project.rest.dto.EmailDto;
import com.example.project.rest.services.RecoverCodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecoverPassController {

    @Autowired
    private RecoverCodeService recoverCodeService;


    @PostMapping("/recover-password")
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid EmailDto dto){
        recoverCodeService.recoverPassword(dto);
        return ResponseEntity.ok().build();
    }

}
