package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Hotel {
    private int id;
    private String name;
    private String address;
    private List<Room> rooms;
    private List<Employee> employees;

    public List<Room> listAvailableRoom() {
        return rooms.stream()
                .filter(r -> r.getStatus().equals(RoomStatus.AVAILABLE))
                .collect(Collectors.toList());
    }

    public void addRoom(Room r) {
        rooms.add(r);
    }

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    public Employee findEmployeeById(int id) {
        return employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public double calculateRevenue(List<Reservation> reservations) {
        return reservations.stream()
                .mapToDouble(Reservation::calculateTotalPrice)
                .sum();
    }

    public double calculateTotalPayroll() {
        return employees.stream()
                .mapToDouble(Employee::calculateRealSalary)
                .sum();
    }
}
