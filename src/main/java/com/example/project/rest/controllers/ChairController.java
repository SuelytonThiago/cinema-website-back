package com.example.project.rest.controllers;

import com.example.project.rest.dto.ChairResponseDto;
import com.example.project.rest.services.ChairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chairs")
public class ChairController {

    @Autowired
    private ChairService chairService;


    @GetMapping("/{id}")
    public ResponseEntity<List<ChairResponseDto>> getAllChairs(@PathVariable Long id){
        return ResponseEntity.ok(chairService.getAllChairs(id));
    }
}
