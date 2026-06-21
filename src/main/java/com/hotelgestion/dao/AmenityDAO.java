package com.hotelgestion.dao;

import com.hotelgestion.model.Amenity;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityDAO implements GenericDAO<Amenity, Integer> {

    @Override
    public Amenity create(Amenity amenity) {
        String sql = "INSERT INTO amenity (name, additional_cost) VALUES (?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, amenity.getName());
            stmt.setDouble(2, amenity.getAdditionalCost());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Amenity(rs.getInt("id"), amenity.getName(), amenity.getAdditionalCost());
                }
            }
            return amenity;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'option (Amenity)", e);
        }
    }

    @Override
    public Optional<Amenity> findById(Integer id) {
        String sql = "SELECT * FROM amenity WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'option " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Amenity> findAll() {
        String sql = "SELECT * FROM amenity";
        List<Amenity> amenities = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                amenities.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les options", e);
        }
        return amenities;
    }

    @Override
    public boolean update(Amenity amenity) {
        String sql = "UPDATE amenity SET name = ?, additional_cost = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, amenity.getName());
            stmt.setDouble(2, amenity.getAdditionalCost());
            stmt.setInt(3, amenity.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'option " + amenity.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM amenity WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'option " + id, e);
        }
    }

    public List<Amenity> findByRoom(int roomId) {
        String sql = """
                SELECT a.id, a.name, a.additional_cost 
                FROM amenity a
                JOIN room_amenity ra ON ra.amenity_id = a.id
                WHERE ra.room_id = ?
                """;
        List<Amenity> amenities = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    amenities.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des options pour la chambre " + roomId, e);
        }
        return amenities;
    }

    private Amenity mapRow(ResultSet rs) throws SQLException {
        return new Amenity(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("additional_cost")
        );
    }
}
