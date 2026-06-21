package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.*;


public class GuestDAO implements GenericDAO<Guest, Integer> {
    @Override
    public Guest create(Guest guest) {
        String sql = """
                INSERT INTO guest (name, phone, email)
                VALUES (?, ?, ?) RETURNING id
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getPhone());
            stmt.setString(3, guest.getEmail());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    guest.setId(rs.getInt("id"));
                }
            }
            return guest;
        } catch (SQLException e) {
            throw new RuntimeException("Error during guest's creation", e);
        }
    }


    @Override
    public Optional<Guest> findById(Integer id) {
        String sql = "SELECT * FROM guest WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error by finding guest id "+id, e);
        }
    }

    @Override
    public List<Guest> findAll() {
        String sql = "SELECT * FROM guest";
        List<Guest> guests = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                guests.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during guest's recuperation", e);
        }
        return guests;
    }

    @Override
    public boolean update(Guest guest) {
        String sql = """
                UPDATE guest SET name = ?, phone = ?, email = ?
                WHERE id = ?
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getPhone());
            stmt.setString(3, guest.getEmail());
            stmt.setInt(4, guest.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating guest "+ guest.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM guest WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting guest "+id, e);
        }
    }

    private Guest mapRow(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setId(rs.getInt("id"));
        guest.setName(rs.getString("name"));
        guest.setPhone(rs.getString("phone"));
        guest.setEmail(rs.getString("email"));

        return guest;
    }
}
