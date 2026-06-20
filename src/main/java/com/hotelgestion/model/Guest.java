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

    public Reservation book(Room r, LocalDate startDate, LocalDate endDate) {
        return new Reservation(0, this, r, startDate, endDate, ReservationStatus.PENDING, 0.0);
    }
}