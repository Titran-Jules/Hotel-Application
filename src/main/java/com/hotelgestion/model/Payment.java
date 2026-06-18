package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Payment {
    private int id;
    private Reservation reservation;
    private double amount;
    private PaymentMethode paymentMethode;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public void validate() {
        this.status = PaymentStatus.VALIDATED;
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
}
