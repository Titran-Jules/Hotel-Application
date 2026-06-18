package com.hotelgestion;


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
