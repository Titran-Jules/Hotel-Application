package com.hotelgestion;

import lombok.Data;

@Data
public abstract class Room {
    private int id;
    private int num;
    private double basePrice;
    private int numbreOfBed;
    private boolean available;

    public abstract double getRealPrice();
}
