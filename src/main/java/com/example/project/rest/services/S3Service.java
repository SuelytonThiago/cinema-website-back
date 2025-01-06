package com.example.project.rest.services;
import com.example.project.rest.services.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    public String bucketName;

    private final S3Client s3Client;

    public S3Service(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        try{
            String fileName = UUID.randomUUID() + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
        }
        catch (IOException e){
            throw new CustomException("algo deu de errado com o upload de imagem: " + e.getMessage());
        }
    }

    public String generatePresignedUrl(String objectKey){
        S3Presigner s3Presigner = S3Presigner.create();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        URL presignedUrl = s3Presigner.presignGetObject(presignRequest).url();

        return presignedUrl.toString();

    }

    public byte[] downloadImage(String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
    }

}
