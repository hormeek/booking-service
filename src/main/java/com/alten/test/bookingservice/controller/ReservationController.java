package com.alten.test.bookingservice.controller;

import com.alten.test.bookingservice.dto.AvailabilityResponseDto;
import com.alten.test.bookingservice.dto.ReservationRequestDto;
import com.alten.test.bookingservice.dto.ReservationResponseDto;
import com.alten.test.bookingservice.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@Validated
public class ReservationController {

    public final ReservationService reservationService;

    @ApiOperation("Get information about rooms availability.")
    @GetMapping
    public AvailabilityResponseDto checkAvailability(@ApiParam("Availability from date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                     @ApiParam("Availability to date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        log.debug("Check availability from: {}, to: {}", start, end);
        return reservationService.checkAvailability(start, end);
    }

    @ApiOperation("Place a new reservation.")
    @PostMapping
    public ReservationResponseDto placeReservation(@ApiParam("Availability dates information") @RequestBody ReservationRequestDto reservationRequestDto) {
        log.debug("Place a new reservation from: {}, to: {}", reservationRequestDto.getReservationStartDateTime(), reservationRequestDto.getReservationEndDateTime());
        return reservationService.placeReservation(reservationRequestDto);
    }

    @ApiOperation("Cancel reservation.")
    @DeleteMapping("/{id}")
    public void cancelReservation(@ApiParam("Reservation id to cancel") @PathVariable UUID id) {
        log.debug("Cancel reservation with id: {}", id);
        reservationService.cancelReservation(id);
    }

    @ApiOperation("Modify reservation.")
    @PutMapping("/{id}")
    public ReservationResponseDto modifyReservation(@ApiParam("Reservation id to modify") @PathVariable UUID id, @ApiParam("Reservation dates to modify") @RequestBody ReservationRequestDto reservationRequestDto) {
        log.debug("Modify reservation with id: {}", id);
        return reservationService.modifyReservation(id, reservationRequestDto);
    }
}
