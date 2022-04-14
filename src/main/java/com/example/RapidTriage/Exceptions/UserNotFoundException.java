package com.example.RapidTriage.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class UserNotFoundException extends HttpClientErrorException {
    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The user with id " + id + " does not exist.");
    }


    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "The user with email " + email + " does not exist.");
    }
}
