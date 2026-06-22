package com.hotelgestion.exception;

import java.sql.SQLException;

public class PaymentFailedException extends RuntimeException{
    public PaymentFailedException(String message) {
        super(message);
    }
    public PaymentFailedException(String message, Exception e) {
        super(message, e);
    }
}
