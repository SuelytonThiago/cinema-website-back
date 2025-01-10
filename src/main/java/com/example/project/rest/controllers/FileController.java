package com.example.project.rest.controllers;
import com.example.project.rest.services.S3Service;
import com.example.project.rest.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UsersService usersService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);

        return ResponseEntity.ok(s3Service.uploadFileUserImg(file,user));
    }


    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(HttpServletRequest request) {
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);



        return ResponseEntity.ok(user.getProfileImg());
    }





}
