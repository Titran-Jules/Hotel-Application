package com.hotelgestion.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Setter
public class Guard extends Employee{
    private String patrolZone;
    private List<DayGuard> dayGuards = new ArrayList<>();
    private double bonusSalaryPerHour;

    @Override
    public double calculateRealSalary() {
        double bonusSalaryPerShiftHour = 15.0;
        double bonusSalary = dayGuards.stream()
                .filter(DayGuard::isShiftHour)
                .mapToLong(DayGuard::getHoursWorked)
                .sum() * bonusSalaryPerHour;

        return getSalary() + bonusSalary;
    }

    public void performPatrol() {
        System.out.println(getName() + " is Performing Patrol at " + getPatrolZone());
    }
}
