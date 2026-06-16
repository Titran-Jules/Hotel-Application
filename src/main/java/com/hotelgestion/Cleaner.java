package com.hotelgestion;


public class Cleaner extends Employee {
    private final double efficacity;
    public Cleaner(int id, String name, String phoneNumber, double salary) {
        super(id,  name, phoneNumber, salary);
        this.efficacity = salary;
    }

    public String cleanUp() {
        return "I cleaned up";
    }

    @Override
    public double calculateRealSalary() {
        return getSalary() * efficacity;
    }
}
