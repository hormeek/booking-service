package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private LocalDateTime bookingStartDateTime;
    private LocalDateTime bookingEndDateTime;
}
