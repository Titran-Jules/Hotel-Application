package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class Employee {
    private int id;
    private String name;
    private String phoneNumber;
    private double salary;

    public abstract double calculateRealSalary();
}
