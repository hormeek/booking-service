package com.alten.test.bookingservice.controller;

import com.alten.test.bookingservice.dto.BookingRequestDto;
import com.alten.test.bookingservice.dto.BookingResponseDto;
import com.alten.test.bookingservice.service.BookingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @ApiOperation("Book the reservation.")
    @PostMapping
    public BookingResponseDto bookReservation(
            @ApiParam("UUID of reservation to book.") @RequestBody BookingRequestDto bookingRequestDto) {
        log.debug("Start booking reservation:{}", bookingRequestDto.getReservationId());
        return bookingService.bookReservation(bookingRequestDto);
    }
}
