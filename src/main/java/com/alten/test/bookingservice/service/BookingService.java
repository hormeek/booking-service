package com.alten.test.bookingservice.service;

import com.alten.test.bookingservice.dto.BookingRequestDto;
import com.alten.test.bookingservice.dto.BookingResponseDto;

/**
 * Interface with booking related operations.
 */
public interface BookingService {
    /**
     * Switch reservation to booked state.
     *
     * @param bookingRequestDto - dto with reservation Id to book.
     * @return created booking details.
     */
    BookingResponseDto bookReservation(BookingRequestDto bookingRequestDto);
}
