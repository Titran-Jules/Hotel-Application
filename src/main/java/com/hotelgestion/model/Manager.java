package com.hotelgestion.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Setter
@Getter
public class Manager extends Employee {
    private List<Employee> employees = new ArrayList<>();

    public String giveOrder() {
        return "I need you to clean the room buddy";
    }

    @Override
    public double calculateRealSalary() {
        return getSalary() + (getEmployees().size() * 100);
    };
}
