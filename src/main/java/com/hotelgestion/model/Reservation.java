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
        if (this.status == ReservationStatus.PENDING
                && this.room.getStatus() == RoomStatus.AVAILABLE
                && this.startDate != null
                && this.endDate != null
                && this.endDate.isAfter(this.startDate)) {
            this.status = ReservationStatus.CONFIRMED;
            this.room.changesStatus(RoomStatus.OCCUPIED);
        }
    }

        public void cancel() {
            if (this.status == ReservationStatus.CONFIRMED
                    || this.status == ReservationStatus.PENDING) {
                this.status = ReservationStatus.CANCELLED;
                this.room.changesStatus(RoomStatus.AVAILABLE);
            }

        }
    }
