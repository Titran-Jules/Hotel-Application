package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
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
        System.out.println(getName() + " is performing Patrol at " + getPatrolZone());
    }
}
