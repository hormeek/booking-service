package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AvailabilityResponseDto {
    private final Set<DatePair> busyDates;
}
