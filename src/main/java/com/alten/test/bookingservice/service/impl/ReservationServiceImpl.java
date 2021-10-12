package com.alten.test.bookingservice.service.impl;

import com.alten.test.bookingservice.dto.AvailabilityResponseDto;
import com.alten.test.bookingservice.dto.DatePair;
import com.alten.test.bookingservice.dto.ReservationRequestDto;
import com.alten.test.bookingservice.dto.ReservationResponseDto;
import com.alten.test.bookingservice.entity.Booking;
import com.alten.test.bookingservice.entity.Reservation;
import com.alten.test.bookingservice.exceptions.BadRequestException;
import com.alten.test.bookingservice.exceptions.NotFoundException;
import com.alten.test.bookingservice.repository.BookingRepository;
import com.alten.test.bookingservice.repository.ReservationRepository;
import com.alten.test.bookingservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookingRepository bookingRepository;

    @Override
    public AvailabilityResponseDto checkAvailability(LocalDate start, LocalDate end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new BadRequestException("Availability dates must be valid.");
        }
        if (start.isBefore(LocalDate.now()) || end.isBefore(LocalDate.now())) {
            throw new BadRequestException("Availability dates must be in future.");
        }
        if (Period.between(start, end).getMonths() > 0) {
            throw new BadRequestException("Maximum availability period is 30 days.");
        }
        List<Reservation> reservationList = reservationRepository.getReservationsBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX));
        List<Booking> bookingList = bookingRepository.getBookingsBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX));
        Set<DatePair> busyDates = Stream.concat(reservationList.stream().map(r -> new DatePair(r.getReservationStartDate().toLocalDate(), r.getReservationEndDate().toLocalDate(), "BUSY")),
                        bookingList.stream().map(b -> new DatePair(b.getBookingStartDate().toLocalDate(), b.getBookingEndDate().toLocalDate(), "BUSY")))
                .collect(Collectors.toSet());
        return new AvailabilityResponseDto(busyDates);
    }

    @Override
    public ReservationResponseDto placeReservation(ReservationRequestDto reservationRequestDto) {
        checkReservationDates(reservationRequestDto);
        Reservation reservation = reservationRepository.save(new Reservation(reservationRequestDto.getReservationStartDateTime(),
                reservationRequestDto.getReservationEndDateTime()));
        return new ReservationResponseDto(reservation.getId(), reservation.getReservationStartDate(), reservation.getReservationEndDate());
    }

    @Override
    public void cancelReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation with id " + id + ", is not found!"));
        reservationRepository.delete(reservation);
    }

    @Override
    public ReservationResponseDto modifyReservation(UUID id, ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation with id " + id + ", is not found!"));
        checkModifyReservationDate(reservationRequestDto, id);
        reservation.setReservationStartDate(reservationRequestDto.getReservationStartDateTime());
        reservation.setReservationEndDate(reservationRequestDto.getReservationEndDateTime());
        return new ReservationResponseDto(id, reservation.getReservationStartDate(), reservation.getReservationEndDate());
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Reservations end date must be after start date.");
        }
        if (end.isBefore(LocalDateTime.now()) || start.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reservation dates must be in future.");
        }
        if (Duration.between(start, end).toHours() > 72) {
            throw new BadRequestException("Reservation duration must be less than 3 days.");
        }
        if (start.equals(end)) {
            throw new BadRequestException("Reservation start and end dates must be different");
        }
        if (LocalDateTime.now().plus(30, ChronoUnit.DAYS).isBefore(end)) {
            throw new BadRequestException("Can’t be reserved more than 30 days in advance");
        }
    }

    private void checkReservationDates(ReservationRequestDto reservationRequestDto) {
        LocalDateTime start = reservationRequestDto.getReservationStartDateTime();
        LocalDateTime end = reservationRequestDto.getReservationEndDateTime();
        checkDates(start, end);
        if (!bookingRepository.getBookingsBetween(start.toLocalDate().atStartOfDay(), end.toLocalDate().atTime(LocalTime.MAX)).isEmpty() ||
                !reservationRepository.getReservationsBetween(start.toLocalDate().atStartOfDay(), end.toLocalDate().atTime(LocalTime.MAX)).isEmpty()) {
            throw new BadRequestException("Dates between " + start + " and " + end + ", is busy.");
        }
    }

    private void checkModifyReservationDate(ReservationRequestDto reservationRequestDto, UUID id) {
        LocalDateTime start = reservationRequestDto.getReservationStartDateTime();
        LocalDateTime end = reservationRequestDto.getReservationEndDateTime();
        checkDates(start, end);
        List<Reservation> reservationList = reservationRepository.getReservationsBetween(start.toLocalDate().atStartOfDay(), end.toLocalDate().atTime(LocalTime.MAX));
        if (!bookingRepository.getBookingsBetween(start.toLocalDate().atStartOfDay(), end.toLocalDate().atTime(LocalTime.MAX)).isEmpty() ||
                !reservationList.isEmpty() && !reservationList.stream().allMatch(reservation -> reservation.getId().equals(id))) {
            throw new BadRequestException("Dates between " + start + " and " + end + ", is busy.");
        }
    }

}
