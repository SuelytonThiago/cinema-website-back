package com.example.project.rest.services;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.services.exceptions.CustomException;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import java.util.UUID;

@Service
public class S3Service {

    private static final Tika tika = new Tika();

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
        if(file.isEmpty()){
            throw new CustomException("select some image");
        }
        try{
            if(!isImage(file)){
                throw new CustomException("File is not a valid image or was renamed incorrectly.");
            }
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
        catch (MaxUploadSizeExceededException e){
            throw new CustomException("maximum upload size exceeded");
        }
    }

    private boolean isImage(MultipartFile file) throws IOException {
        String fileType = tika.detect(file.getInputStream());

        return fileType.startsWith("image/");
    }
}
