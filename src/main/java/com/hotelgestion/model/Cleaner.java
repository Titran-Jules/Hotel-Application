package com.hotelgestion.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class Cleaner extends Employee {
    private double efficacity;

    public void clean(Room r) {
        r.changesStatus(RoomStatus.CLEANING);
    }

    @Override
    public double calculateRealSalary() {
        return getSalary() * efficacity;
    }
}
