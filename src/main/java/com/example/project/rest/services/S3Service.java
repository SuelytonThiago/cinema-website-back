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

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MessageSource messageSource;


    @Transactional
    public String uploadFileUserImg(MultipartFile file, Users user) {
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

            var url =  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            user.setProfileImg(url);
            usersRepository.save(user);
            return user.getProfileImg();
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

    @Transactional
    public String uploadFileMovieImg(MultipartFile file, Movies movie) {
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

            var url =  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            movie.setImageUrl(url);
            movieRepository.save(movie);
            return movie.getImageUrl();
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

    private boolean isImage(MultipartFile file) throws IOException {
        String fileType = tika.detect(file.getInputStream());

        return fileType.startsWith("image/");
    }
}
