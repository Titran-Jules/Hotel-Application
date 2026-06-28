package com.hotelgestion.service;

import com.hotelgestion.dao.EmployeeDAO;
import com.hotelgestion.dao.RoomDAO;
import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final RoomDAO roomDAO;

    public EmployeeService(EmployeeDAO employeeDAO, RoomDAO roomDAO) {
        this.employeeDAO = employeeDAO;
        this.roomDAO = roomDAO;
    }

    public double calculateTotalPayroll(int hotelId) {
        List<Employee> employees = employeeDAO.findByHotelId(hotelId);
        return employees.stream()
                .mapToDouble(Employee::calculateRealSalary)
                .sum();
    }

    public void assignCleanerToRoom(int cleanerId, int roomId) {
        var employee = employeeDAO.findById(cleanerId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec id " + cleanerId));
        if (!(employee instanceof Cleaner)) {
            throw new IllegalArgumentException("L'employé avec l'id " + cleanerId + " n'est pas un Cleaner.");
        }
        var cleaner = (Cleaner) employee;
        var room = roomDAO.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'id " + roomId));
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                cleaner.clean(room);
                roomDAO.update(room);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'assignation du nettoyeur", e);
        }
    }

    public Employee hire(Employee e, int managerId, int hotelId) {
        return employeeDAO.create(e, managerId, hotelId);
    }
}
