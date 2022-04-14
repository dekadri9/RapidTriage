package com.example.RapidTriage.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

@ResponseBody
public class AlreadyLoginException extends HttpClientErrorException {
    public AlreadyLoginException(){
        super(HttpStatus.CONFLICT, "This user is already logged in.");
    }
}
