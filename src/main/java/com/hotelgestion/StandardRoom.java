package com.hotelgestion;

import lombok.Getter;

import java.util.List;

@Getter
public class StandardRoom extends Room {
    public StandardRoom(int id, int num, double basePrice, int numberOfBed, boolean available, List<Furniture> furnitures) {
        super(id, num, basePrice, numberOfBed, available, furnitures);
    }

    @Override
    public double getRealPrice() {
        return this.getBasePrice();
    }
}
