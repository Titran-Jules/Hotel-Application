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
        int nbSubordonnes = (this.employees != null) ? this.employees.size() : 0;
        return getSalary() + (nbSubordonnes * 25000);
    };

    public boolean validateReservation(Reservation r) {
        if (r.getStatus().equals(ReservationStatus.PENDING)) {
            r.setStatus(ReservationStatus.CONFIRMED);
            return true;
        } else {
            return false;
        }
    }

    public void orderCleaning(Cleaner c, Room r) {
        c.clean(r);
    }

    public void addTeamMember(Employee e) {
        employees.add(e);
    }
}
