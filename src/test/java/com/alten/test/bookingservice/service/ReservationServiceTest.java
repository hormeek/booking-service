package com.alten.test.bookingservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alten.test.bookingservice.dto.ReservationRequestDto;
import com.alten.test.bookingservice.dto.ReservationResponseDto;
import com.alten.test.bookingservice.entity.Booking;
import com.alten.test.bookingservice.entity.Reservation;
import com.alten.test.bookingservice.exceptions.BadRequestException;
import com.alten.test.bookingservice.exceptions.NotFoundException;
import com.alten.test.bookingservice.repository.BookingRepository;
import com.alten.test.bookingservice.repository.ReservationRepository;
import com.alten.test.bookingservice.service.impl.ReservationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionIfReservationNotFoundWhenCancelling() {
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());
        reservationService.cancelReservation(UUID.randomUUID());
    }

    @Test
    public void shouldCancelReservation() {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = new Reservation(LocalDateTime.now().plus(1, ChronoUnit.DAYS), LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        when(reservationRepository.findById(eq(reservationId))).thenReturn(Optional.of(reservation));
        reservationService.cancelReservation(reservationId);
        verify(reservationRepository).delete(eq(reservation));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionIfReservationNotFoundWhenModifying() {
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());
        reservationService.modifyReservation(UUID.randomUUID(), null);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionWhenDatesAreEqual() {
        reservationService.checkAvailability(LocalDate.now(), LocalDate.now());
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionWhenDatesInvalid() {
        reservationService.checkAvailability(LocalDate.now(), LocalDate.now().minus(1, ChronoUnit.DAYS));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestIfAvailabilityStartDateInPast() {
        reservationService.checkAvailability(LocalDate.now().minus(1, ChronoUnit.DAYS), LocalDate.now());
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestIfAvailabilityEndDateInPast() {
        reservationService.checkAvailability(LocalDate.now(), LocalDate.now().minus(1, ChronoUnit.DAYS));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestIfAvailabilityDaysPeriodMoreThanMonth() {
        reservationService.checkAvailability(LocalDate.now(), LocalDate.now().plus(1, ChronoUnit.MONTHS));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenReservationEndDateAfterStartDate() {
        reservationService.placeReservation(new ReservationRequestDto(LocalDateTime.now(), LocalDateTime.now().minus(1, ChronoUnit.DAYS)));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenStartDateInPast() {
        reservationService.placeReservation(new ReservationRequestDto(LocalDateTime.now().minus(1, ChronoUnit.DAYS), LocalDateTime.now()));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenEndDateInPast() {
        reservationService.placeReservation(new ReservationRequestDto(LocalDateTime.now(), LocalDateTime.now().minus(1, ChronoUnit.DAYS)));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenReservationMoreThanThreeDays() {
        reservationService.placeReservation(new ReservationRequestDto(LocalDateTime.now(), LocalDateTime.now().plus(4, ChronoUnit.DAYS)));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenReservationDatesAreEqual() {
        LocalDateTime equalDate = LocalDateTime.now();
        reservationService.placeReservation(new ReservationRequestDto(equalDate, equalDate));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenReservationDatesAreBusy() {
        LocalDateTime startDate = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        when(reservationRepository.getReservationsBetween(any(), any())).thenReturn(List.of(new Reservation()));
        reservationService.placeReservation(new ReservationRequestDto(startDate, startDate.plus(1, ChronoUnit.DAYS)));
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestWhenReservationDatesAreBusyByBooking() {
        LocalDateTime startDate = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        when(bookingRepository.getBookingsBetween(any(), any())).thenReturn(List.of(new Booking()));
        reservationService.placeReservation(new ReservationRequestDto(startDate, startDate.plus(1, ChronoUnit.DAYS)));
    }

    @Test
    public void shouldModifyBusyReservationDateIfSameReservation() {
        UUID existingReservation = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        Reservation reservation = new Reservation();
        reservation.setId(existingReservation);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(bookingRepository.getBookingsBetween(any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.getReservationsBetween(any(), any())).thenReturn(Collections.singletonList(reservation));
        ReservationResponseDto dto = reservationService.modifyReservation(existingReservation, new ReservationRequestDto(startDate, startDate.plus(1, ChronoUnit.DAYS)));
        assertEquals(new ReservationResponseDto(existingReservation, startDate, startDate.plus(1, ChronoUnit.DAYS)), dto);
    }
}