package com.example.project.rest.services.exceptions;

public class NotAuthenticatedException extends RuntimeException{

    public NotAuthenticatedException(String message) {
        super(message);
    }
}
