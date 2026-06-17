package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor

public class Reservation {

    private int id;
    private Guest guest;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;
    private double totalPrice;

    public long calculateNumberOfNights() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public double calculateTotalPrice() {
        return calculateNumberOfNights() * room.calculateActualPrice();
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}