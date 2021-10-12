package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    private UUID reservationId;
    private LocalDateTime reservationStartDateTime;
    private LocalDateTime reservationEndDateTime;
}
