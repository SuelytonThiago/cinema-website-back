package com.example.project.rest.services;
import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.MovieRepository;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.services.exceptions.CustomException;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
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
    private MessageSource messageSource;


    @Transactional
    public String uploadFileImg(MultipartFile file) {
        if(file.isEmpty()){
            throw new CustomException(
                    messageSource.getMessage("s3.service.error.selectImg", null, LocaleContextHolder.getLocale())
            );
        }
        try{
            if(!isImage(file)){

                throw new CustomException(
                        messageSource.getMessage("s3.service.error.invalidImg", null, LocaleContextHolder.getLocale())
                );
            }
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

            throw new CustomException(
                    messageSource.getMessage("s3.service.error.sendingImg", null, LocaleContextHolder.getLocale())+ e.getMessage());
        }
        catch (MaxUploadSizeExceededException e){
            throw new CustomException(
                    messageSource.getMessage("s3.service.error.maxUpload", null, LocaleContextHolder.getLocale())
            );
        }
    }

    public String updateMovieImgFile(MultipartFile file, String fileId) throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        file.transferTo(tempFile);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileId)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(tempFile));

        String fileName = UUID.randomUUID() + file.getOriginalFilename();


        Files.delete(tempFile);

        return  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    private boolean isImage(MultipartFile file) throws IOException {
        String fileType = tika.detect(file.getInputStream());

        return fileType.startsWith("image/");
    }
}
