package com.alten.test.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UUIDResponseDto {
    private final UUID id;
}
