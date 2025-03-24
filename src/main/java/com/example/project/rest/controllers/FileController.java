package com.example.project.rest.controllers;
import com.example.project.rest.services.S3Service;
import com.example.project.rest.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/files")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UsersService usersService;

    @PostMapping("/upload")
    @Operation(summary = "insert file into aws bucket")
    public ResponseEntity<String> uploadUserFile(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
        return ResponseEntity.ok(usersService.uploadUserFile(file,request));
    }

    @GetMapping("/presigned-url")
    @Operation(summary = "get the image url from within the aws bucket")
    public ResponseEntity<String> getPresignedUrl(HttpServletRequest request) {
        var authHeader =request.getHeader("Authorization");
        var user = usersService.getUserAuthenticated(authHeader);
        return ResponseEntity.ok(user.getProfileImg());
    }





}
