package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class DatePair {
    private final LocalDate from;
    private final LocalDate to;
    private final String state;
}
