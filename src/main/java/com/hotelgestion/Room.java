package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class Room {
    private int id;
    private int num;
    private double basePrice;
    private int numberOfBed;
    private boolean available;
    private List<Furniture> furnitures;

    public abstract double getRealPrice();
}
