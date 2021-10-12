package com.alten.test.bookingservice.repository;

import com.alten.test.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    @Query("FROM Booking WHERE bookingStartDate <= :end AND bookingEndDate >= :start")
    List<Booking> getBookingsBetween(LocalDateTime start, LocalDateTime end);
}
