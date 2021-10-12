package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookingRequestDto {
    @NotNull
    private UUID reservationId;
}
