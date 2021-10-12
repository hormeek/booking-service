package com.alten.test.bookingservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alten.test.bookingservice.dto.BookingRequestDto;
import com.alten.test.bookingservice.entity.Booking;
import com.alten.test.bookingservice.entity.Reservation;
import com.alten.test.bookingservice.exceptions.BadRequestException;
import com.alten.test.bookingservice.exceptions.NotFoundException;
import com.alten.test.bookingservice.repository.BookingRepository;
import com.alten.test.bookingservice.repository.ReservationRepository;
import com.alten.test.bookingservice.service.impl.BookingServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingServiceimpl;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    public void shouldSaveBookingAndDeleteReservation() {
        UUID reservationId = UUID.randomUUID();
        Reservation res = new Reservation(LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        Booking booking = new Booking(res.getReservationStartDate(), res.getReservationEndDate());
        when(reservationRepository.findById(any())).thenReturn(Optional.of(res));
        bookingServiceimpl.bookReservation(new BookingRequestDto(reservationId));

        verify(reservationRepository).delete(eq(res));
        verify(bookingRepository).save(eq(booking));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenReservationNotFound() {
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());
        bookingServiceimpl.bookReservation(new BookingRequestDto(UUID.randomUUID()));
    }

    @Test(expected = BadRequestException.class)
    public void shouldNotBookExpiredReservation() {
        UUID reservationId = UUID.randomUUID();
        Reservation res = new Reservation(LocalDateTime.now().minus(2,ChronoUnit.DAYS), LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        when(reservationRepository.findById(any())).thenReturn(Optional.of(res));
        bookingServiceimpl.bookReservation(new BookingRequestDto(reservationId));
    }

}