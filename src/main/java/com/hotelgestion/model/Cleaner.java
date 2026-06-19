package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Cleaner extends Employee {
    private double efficacity;

    public String cleanUp() {
        return "I cleaned up";
    }

    @Override
    public double calculateRealSalary() {
        return getSalary() * efficacity;
    }
}
