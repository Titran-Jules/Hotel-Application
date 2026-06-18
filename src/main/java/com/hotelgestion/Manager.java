package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Manager extends Employee {
    private final List<Employee> employees = new ArrayList<>();

    public String giveOrder() {
        return "I need you to clean the room buddy";
    }

    @Override
    public double calculateRealSalary() {
        return getSalary() + (getEmployees().size() * 100);
    };
}
