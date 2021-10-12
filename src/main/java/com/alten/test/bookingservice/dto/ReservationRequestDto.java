package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {
    @NotNull
    private LocalDateTime reservationStartDateTime;
    @NotNull
    private LocalDateTime reservationEndDateTime;
}
