package com.alten.test.bookingservice.repository;

import com.alten.test.bookingservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("FROM Reservation WHERE reservationStartDate <= :end AND reservationEndDate >= :start")
    List<Reservation> getReservationsBetween(LocalDateTime start, LocalDateTime end);
}
