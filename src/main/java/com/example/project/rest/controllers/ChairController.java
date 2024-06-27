package com.example.project.rest.controllers;

import com.example.project.rest.dto.ChairRequestDto;
import com.example.project.rest.services.ChairService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chairs")
public class ChairController {

    @Autowired
    private ChairService chairService;


    @PostMapping("/add")
    public ResponseEntity<Void> reserveChair(@RequestBody @Valid ChairRequestDto dto){
        chairService.addChair(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
