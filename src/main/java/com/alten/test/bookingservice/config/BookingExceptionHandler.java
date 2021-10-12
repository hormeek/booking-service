package com.alten.test.bookingservice.config;

import com.alten.test.bookingservice.exceptions.BadRequestException;
import com.alten.test.bookingservice.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookingExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<HttpResponse> handleException(NotFoundException notFoundException) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.NOT_FOUND, notFoundException.getMessage());
        return new ResponseEntity<>(httpResponse, httpResponse.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<HttpResponse> handleException(BadRequestException badRequestException) {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.BAD_REQUEST, badRequestException.getMessage());
        return new ResponseEntity<>(httpResponse, httpResponse.getHttpStatus());
    }
}
