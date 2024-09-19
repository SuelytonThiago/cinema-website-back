package com.example.project.rest.controllers;

import com.example.project.domain.entities.Categories;
import com.example.project.rest.dto.CategoryRequestDto;
import com.example.project.rest.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;


    @PostMapping("/add")
    public ResponseEntity<Void> addNewCategory(@RequestBody @Valid CategoryRequestDto dto){
        service.addNewCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Categories>> findAll(){
        return ResponseEntity.ok(service.findAllCategories());
    }
}
