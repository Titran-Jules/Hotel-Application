package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelDAO implements GenericDAO<Hotel, Integer> {

    private final RoomDAO roomDAO = new RoomDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public Hotel create(Hotel hotel) {
        String sql = "INSERT INTO hotel (name, address) VALUES (?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getAddress());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hotel.setId(rs.getInt("id"));
                }
            }
            if (hotel.getRooms() == null) hotel.setRooms(new ArrayList<>());
            if (hotel.getEmployees() == null) hotel.setEmployees(new ArrayList<>());

            return hotel;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'hôtel", e);
        }
    }

    @Override
    public Optional<Hotel> findById(Integer id) {
        String sql = "SELECT * FROM hotel WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Hotel hotel = mapRow(rs);
                    return Optional.of(hotel);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'hôtel " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Hotel> findAll() {
        String sql = "SELECT * FROM hotel";
        List<Hotel> hotels = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hotels.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des hôtels", e);
        }
        return hotels;
    }

    @Override
    public boolean update(Hotel hotel) {
        String sql = "UPDATE hotel SET name = ?, address = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getAddress());
            stmt.setInt(3, hotel.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'hôtel " + hotel.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM hotel WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'hôtel " + id, e);
        }
    }

    public Optional<Hotel> findWithRoomsAndEmployees(int id) {
        Optional<Hotel> hotelOpt = findById(id);

        if (hotelOpt.isPresent()) {
            var hotel = hotelOpt.get();

            List<Room> rooms = roomDAO.findByHotelId(id);
            hotel.setRooms(rooms);

            List<Employee> employees = findEmployeesByHotelId(id);
            hotel.setEmployees(employees);

            return Optional.of(hotel);
        }

        return Optional.empty();
    }

    private List<Employee> findEmployeesByHotelId(int hotelId) {
       return employeeDAO.findByHotelId(hotelId);
    }

    private Hotel mapRow(ResultSet rs) throws SQLException {
        return new Hotel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
