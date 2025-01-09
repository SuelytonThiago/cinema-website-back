package com.example.project.rest.services;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.services.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
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

    @Value("${aws.s3.access-key}")
    public String accessKey;

    @Value("${aws.s3.secret-key}")
    public String secretKey;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public String uploadFileUserImg(MultipartFile file, Users user) {
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/gif") &&
                        !contentType.equals("image/webp"))) {
            throw new CustomException("The file must be in jpeg, png, gif or webp format");
        }
        try{
            String fileName = UUID.randomUUID() + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            var url =  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            user.setProfileImg(url);
            usersRepository.save(user);
            return user.getProfileImg();
        }
        catch (IOException e){
            throw new CustomException("something went wrong with the image upload: " + e.getMessage());
        }
    }
}
