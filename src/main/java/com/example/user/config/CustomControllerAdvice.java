package com.example.user.config;

import com.example.user.exception.InvalidPayloadException;
import com.example.user.exception.InvalidStateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class CustomControllerAdvice {
    private final MediaType vndErrorMediaType =
            MediaType.parseMediaType("application/vnd.error");

    @ExceptionHandler(value = InvalidStateException.class)
    ResponseEntity<Object> invalidStateException(Exception e) {
        return this.error(e, HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidPayloadException.class)
    ResponseEntity<Object> invalidPayloadException(InvalidPayloadException e) {
        return this.error(e, HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    private <E extends Exception> ResponseEntity<Object> error(E error, HttpStatus httpStatus, String msg) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(this.vndErrorMediaType);
        return new ResponseEntity<>(msg,
                httpHeaders, httpStatus);
    }

}
