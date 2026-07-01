package com.hotelgestion.model;

public interface PaymentMethode {
    String getProviderName();
    void processPayment(double amount);
}
