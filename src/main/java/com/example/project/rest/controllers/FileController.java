package com.example.project.rest.controllers;

import com.example.project.rest.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(s3Service.uploadFile(file));
    }


    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String objectKey) {
        String url = s3Service.generatePresignedUrl(objectKey);
        return ResponseEntity.ok(url);
    }


    @GetMapping("/{objectKey}")
    public ResponseEntity<byte[]> getImage(@PathVariable String objectKey) {
        byte[] imageData = s3Service.downloadImage(objectKey);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }




}
