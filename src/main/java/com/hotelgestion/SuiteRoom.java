package com.hotelgestion;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SuiteRoom extends Room {
    private int numberOfRoom;
    private boolean jacouzi;

    public SuiteRoom(int id, int num, double basePrice, int numberOfBed, boolean available, List<Furniture> furnitures) {
        super(id, num, basePrice, numberOfBed, available, furnitures);
    }

    @Override
    public double getRealPrice() {
        return isJacouzi() ? getBasePrice() + (50*numberOfRoom) + (100) : getBasePrice() + (50*numberOfRoom);
    }
}
