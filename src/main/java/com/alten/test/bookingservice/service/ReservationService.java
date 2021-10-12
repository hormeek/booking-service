package com.alten.test.bookingservice.service;

import com.alten.test.bookingservice.dto.AvailabilityResponseDto;
import com.alten.test.bookingservice.dto.ReservationRequestDto;
import com.alten.test.bookingservice.dto.ReservationResponseDto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Interface with reservation related operations.
 */
public interface ReservationService {
    /**
     * Check room availability within specific date range.
     *
     * @param start - range start date
     * @param end   - range end date
     * @return availability status
     */
    AvailabilityResponseDto checkAvailability(LocalDate start, LocalDate end);

    /**
     * Place new a reservation.
     *
     * @param reservationRequestDto - reservation information to create
     * @return created reservation information
     */
    ReservationResponseDto placeReservation(ReservationRequestDto reservationRequestDto);

    /**
     * Cancel reservation.
     *
     * @param id reservation id to cancel.
     */
    void cancelReservation(UUID id);

    /**
     * Modify reservation.
     *
     * @param id - reservation id to modify
     * @param reservationRequestDto - reservation information to modify
     * @return modified reservation information
     */
    ReservationResponseDto modifyReservation(UUID id, ReservationRequestDto reservationRequestDto);
}
