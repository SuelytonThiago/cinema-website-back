package com.example.project.rest.controllers.exceptions;

import com.example.project.rest.services.exceptions.AlreadyExistsExceptions;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ObjectNotFoundExceptions.class)
    public ProblemDetail objectNotFound(ObjectNotFoundExceptions e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setProperty("TimeStamp", LocalDate.now());
        problemDetail.setProperty("Message",e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(AlreadyExistsExceptions.class)
    public ProblemDetail alreadyExists(AlreadyExistsExceptions e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("TimeStamp",LocalDate.now());
        problemDetail.setProperty("Message",e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgument(MethodArgumentNotValidException e){
        List<String> err = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("TimeStamp",LocalDate.now());
        problemDetail.setProperty("Message",err);
        return problemDetail;

    }
}
