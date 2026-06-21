package com.hotelgestion.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public abstract class Employee {
    private int id;
    private String name;
    private String phoneNumber;
    private double salary;
    private int hotelId;
    private int managerId;

    public abstract double calculateRealSalary();
}
