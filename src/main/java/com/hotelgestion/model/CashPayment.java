package com.hotelgestion.model;

public class CashPayment implements PaymentMethode {
    @Override
    public void processPayment(double amount) {
        System.out.println("Amount of :"+ amount+ " Ar has been paid with success");
    }
}
