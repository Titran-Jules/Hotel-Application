package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Manager extends Employee {
    private List<Employee> employees = new ArrayList<>();

    public Manager(int id, String name, String phoneNumber, double salary, List<Employee> employees) {
        super(id, name, phoneNumber, salary);
        this.employees = employees;
    }

    public String giveOrder() {
        return "I need you to clean the chamber buddy";
    }

    @Override
    public double calculateRealSalary() {
        return getSalary() + (getEmployees().size() * 100);
    };
}
