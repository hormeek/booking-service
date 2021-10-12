package com.alten.test.bookingservice.service.impl;

import com.alten.test.bookingservice.dto.BookingRequestDto;
import com.alten.test.bookingservice.dto.BookingResponseDto;
import com.alten.test.bookingservice.entity.Booking;
import com.alten.test.bookingservice.entity.Reservation;
import com.alten.test.bookingservice.exceptions.BadRequestException;
import com.alten.test.bookingservice.exceptions.NotFoundException;
import com.alten.test.bookingservice.repository.BookingRepository;
import com.alten.test.bookingservice.repository.ReservationRepository;
import com.alten.test.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public BookingResponseDto bookReservation(BookingRequestDto bookingRequestDto) {
        Reservation reservation = reservationRepository.findById(bookingRequestDto.getReservationId()).orElseThrow(() -> new NotFoundException("Reservation with id " + bookingRequestDto.getReservationId() + ", is not found!"));
        if (reservation.getReservationEndDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reservation expired.");
        }
        bookingRepository.save(new Booking(reservation.getReservationStartDate(), reservation.getReservationEndDate()));
        reservationRepository.delete(reservation);
        return new BookingResponseDto(reservation.getReservationStartDate(), reservation.getReservationEndDate());
    }
}
