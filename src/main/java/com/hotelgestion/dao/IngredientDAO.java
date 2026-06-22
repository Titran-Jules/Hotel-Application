package com.hotelgestion.dao;

import com.hotelgestion.model.Ingredient;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientDAO implements GenericDAO<Ingredient, Integer> {

    @Override
    public Ingredient create(Ingredient ing) {
        String sql = "INSERT INTO ingredient (name, stock_quantity, unit, alert_threshold) VALUES (?, ?, ?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ing.getName());
            stmt.setDouble(2, ing.getStockQuantity());
            stmt.setString(3, ing.getUnit());
            stmt.setDouble(4, ing.getAlertThreshold());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) ing.setId(rs.getInt("id"));
            }
            return ing;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'ingrédient", e);
        }
    }

    @Override
    public Optional<Ingredient> findById(Integer id) {
        String sql = "SELECT * FROM ingredient WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById ingrédient " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Ingredient> findAll() {
        String sql = "SELECT * FROM ingredient";
        List<Ingredient> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll ingrédients", e);
        }
        return list;
    }

    @Override
    public boolean update(Ingredient ing) {
        String sql = "UPDATE ingredient SET name = ?, stock_quantity = ?, unit = ?, alert_threshold = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ing.getName());
            stmt.setDouble(2, ing.getStockQuantity());
            stmt.setString(3, ing.getUnit());
            stmt.setDouble(4, ing.getAlertThreshold());
            stmt.setInt(5, ing.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update ingrédient " + ing.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM ingredient WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete ingrédient " + id, e);
        }
    }

    public List<Ingredient> findLowStock() {
        String sql = "SELECT * FROM ingredient WHERE stock_quantity <= alert_threshold";
        List<Ingredient> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du stock bas", e);
        }
        return list;
    }

    private Ingredient mapRow(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("stock_quantity"),
                rs.getString("unit"),
                rs.getDouble("alert_threshold")
        );
    }
}
