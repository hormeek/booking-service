package com.alten.test.bookingservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class HttpResponse {
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
