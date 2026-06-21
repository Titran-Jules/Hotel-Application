package com.hotelgestion.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Guest {
    private int id;
    private String name;
    private String phone;
    private String email;

    public Reservation book(int reservationId, Room r, LocalDate startDate, LocalDate endDate) {
        Reservation reservation = new Reservation(reservationId, this, r, startDate, endDate, ReservationStatus.PENDING, 0.0);
        reservation.setTotalPrice(reservation.calculateTotalPrice());
        return reservation;
    }
}