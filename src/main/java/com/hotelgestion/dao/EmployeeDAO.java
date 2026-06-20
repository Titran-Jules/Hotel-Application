package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO implements GenericDAO<Employee, Integer> {

    @Override
    public Employee create(Employee emp) {
        String sql = """
                INSERT INTO employee (name, phone, salary, employee_type, efficiency, patrol_zone, specialty, bonus_salary_per_hour, manager_id, hotel_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getPhoneNumber());
            stmt.setDouble(3, emp.getSalary());

            if (emp instanceof Manager) {
                stmt.setString(4, "MANAGER");
                stmt.setNull(5, Types.NUMERIC);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.NUMERIC);
            } else if (emp instanceof Cleaner cleaner) {
                stmt.setString(4, "CLEANER");
                stmt.setDouble(5, cleaner.getEfficacity());
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.NUMERIC);
            } else if (emp instanceof Guard guard) {
                stmt.setString(4, "GUARD");
                stmt.setNull(5, Types.NUMERIC);
                stmt.setString(6, guard.getPatrolZone());
                stmt.setNull(7, Types.VARCHAR);
                stmt.setDouble(8, guard.getBonusSalaryPerHour());
            } else if (emp instanceof Cook cook) {
                stmt.setString(4, "COOK");
                stmt.setNull(5, Types.NUMERIC);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setString(7, cook.getSpeciality());
                stmt.setNull(8, Types.NUMERIC);
            }

            if (emp.getManagerId() > 0) {
                stmt.setInt(9, emp.getManagerId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            stmt.setInt(10, emp.getHotelId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emp.setId(rs.getInt("id"));
                }
            }
            return emp;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'employé", e);
        }
    }

    @Override
    public Optional<Employee> findById(Integer id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'employé " + id, e);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        List<Employee> employees = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employees.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des employés", e);
        }
        return employees;
    }

    @Override
    public boolean update(Employee emp) {
        String sql = """
                UPDATE employee SET name = ?, phone = ?, salary = ?, efficacity = ?, 
                                    patrol_zone = ?, specialty = ?, bonus_salary_per_hour = ?, 
                                    manager_id = ?, hotel_id = ? WHERE id = ?
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getPhoneNumber());
            stmt.setDouble(3, emp.getSalary());

            if (emp instanceof Cleaner cleaner) {
                stmt.setDouble(4, cleaner.getEfficacity());
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.NUMERIC);
            } else if (emp instanceof Guard guard) {
                stmt.setNull(4, Types.NUMERIC);
                stmt.setString(5, guard.getPatrolZone());
                stmt.setNull(6, Types.VARCHAR);
                stmt.setDouble(7, guard.getBonusSalaryPerHour());
            } else if (emp instanceof Cook cook) {
                stmt.setNull(4, Types.NUMERIC);
                stmt.setNull(5, Types.VARCHAR);
                stmt.setString(6, cook.getSpeciality());
                stmt.setNull(7, Types.NUMERIC);
            } else { // Manager
                stmt.setNull(4, Types.NUMERIC);
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.NUMERIC);
            }

            if (emp.getManagerId() > 0) {
                stmt.setInt(8, emp.getManagerId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }
            stmt.setInt(9, emp.getHotelId());
            stmt.setInt(10, emp.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'employé " + emp.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'employé " + id, e);
        }
    }

    public List<Employee> findByType(Class<? extends Employee> type) {
        String dbType = "";
        if (type == Manager.class) dbType = "MANAGER";
        else if (type == Cleaner.class) dbType = "CLEANER";
        else if (type == Guard.class) dbType = "GUARD";
        else if (type == Cook.class) dbType = "COOK";

        String sql = "SELECT * FROM employee WHERE employee_type = ?";
        List<Employee> employees = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dbType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage par type d'employé", e);
        }
        return employees;
    }

    public List<Employee> findManagerTeam(int managerId) {
        String sql = "SELECT * FROM employee WHERE manager_id = ?";
        List<Employee> team = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    team.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'équipe du manager " + managerId, e);
        }
        return team;
    }

    private List<DayGuard> findShiftsForGuard(int guardId) {
        String sql = "SELECT date, start_time, end_time, shift_hour FROM day_guard WHERE guard_id = ?";
        List<DayGuard> shifts = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shifts.add(DayGuard.builder()
                            .date(rs.getDate("date").toLocalDate())
                            .startTime(rs.getTime("start_time").toLocalTime())
                            .endTime(rs.getTime("end_time").toLocalTime())
                            .shiftHour(rs.getBoolean("shift_hour"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des shifts du garde " + guardId, e);
        }
        return shifts;
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        String empType = rs.getString("employee_type");
        Employee emp;

        switch (empType) {
            case "MANAGER" -> {
                Manager manager = Manager.builder().build();
                emp = manager;
            }
            case "CLEANER" -> {
                Cleaner cleaner = Cleaner.builder().build();
                cleaner.setEfficacity(rs.getDouble("efficiency"));
                emp = cleaner;
            }
            case "GUARD" -> {
                Guard guard = Guard.builder().build();
                guard.setPatrolZone(rs.getString("patrol_zone"));
                guard.setBonusSalaryPerHour(rs.getDouble("bonus_salary_per_hour"));
                guard.setDayGuards(findShiftsForGuard(rs.getInt("id")));
                emp = guard;
            }
            case "COOK" -> {
                Cook cook = Cook.builder().build();
                cook.setSpeciality(rs.getString("specialty"));
                emp = cook;
            }
            default -> throw new IllegalArgumentException("Type d'employé inconnu: " + empType);
        }

        emp.setId(rs.getInt("id"));
        emp.setName(rs.getString("name"));
        emp.setPhoneNumber(rs.getString("phone"));
        emp.setSalary(rs.getDouble("salary"));
        emp.setHotelId(rs.getInt("hotel_id"));
        emp.setManagerId(rs.getInt("manager_id"));

        return emp;
    }
}
