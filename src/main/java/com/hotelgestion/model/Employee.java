package com.hotelgestion.model;

import lombok.*;

@NoArgsConstructor
@Data
public abstract class Employee {
    private int id;
    private String name;
    private String phoneNumber;
    private double salary;

    public abstract double calculateRealSalary();
}
