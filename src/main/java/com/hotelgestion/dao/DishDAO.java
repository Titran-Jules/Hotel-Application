package com.hotelgestion.dao;

import com.hotelgestion.model.Dish;
import com.hotelgestion.model.DishCategory;
import com.hotelgestion.model.Ingredient;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DishDAO implements GenericDAO<Dish, Integer> {

    @Override
    public Dish create(Dish dish) {
        String sql = "INSERT INTO dish (name, price, category, preparation_time_minutes) VALUES (?, ?, ?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dish.getName());
            stmt.setDouble(2, dish.getPrice());
            stmt.setString(3, dish.getCategory().name());
            stmt.setInt(4, dish.getPreparationTimeMinutes());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) dish.setId(rs.getInt("id"));
            }
            saveIngredients(dish);
            return dish;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création plat", e);
        }
    }

    @Override
    public Optional<Dish> findById(Integer id) {
        String sql = "SELECT * FROM dish WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById plat", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Dish> findAll() {
        String sql = "SELECT * FROM dish";
        List<Dish> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll plats", e);
        }
        return list;
    }

    @Override
    public boolean update(Dish dish) {
        String sql = "UPDATE dish SET name = ?, price = ?, category = ?, preparation_time_minutes = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dish.getName());
            stmt.setDouble(2, dish.getPrice());
            stmt.setString(3, dish.getCategory().name());
            stmt.setInt(4, dish.getPreparationTimeMinutes());
            stmt.setInt(5, dish.getId());

            boolean updated = stmt.executeUpdate() > 0;
            if (updated) {
                String deleteJoin = "DELETE FROM dish_ingredient WHERE dish_id = ?";
                try (PreparedStatement delStmt = conn.prepareStatement(deleteJoin)) {
                    delStmt.setInt(1, dish.getId());
                    delStmt.executeUpdate();
                }
                saveIngredients(dish);
            }
            return updated;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update plat", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM dish WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete plat", e);
        }
    }

    public List<Dish> findByCategory(DishCategory category) {
        String sql = "SELECT * FROM dish WHERE category = ?";
        List<Dish> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByCategory plats", e);
        }
        return list;
    }

    private void saveIngredients(Dish dish) throws SQLException {
        if (dish.getRequiredIngredients() == null) return;
        String sql = "INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity) VALUES (?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Map.Entry<Ingredient, Double> entry : dish.getRequiredIngredients().entrySet()) {
                stmt.setInt(1, dish.getId());
                stmt.setInt(2, entry.getKey().getId());
                stmt.setDouble(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Map<Ingredient, Double> loadIngredientsForDish(int dishId) {
        String sql = """
                SELECT i.*, di.quantity FROM ingredient i
                JOIN dish_ingredient di ON di.ingredient_id = i.id
                WHERE di.dish_id = ?
                """;
        Map<Ingredient, Double> ingredients = new HashMap<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dishId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("stock_quantity"),
                            rs.getString("unit"),
                            rs.getDouble("alert_threshold")
                    );
                    ingredients.put(ing, rs.getDouble("quantity"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement ingrédients pour le plat " + dishId, e);
        }
        return ingredients;
    }

    private Dish mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return new Dish(
                id,
                rs.getString("name"),
                rs.getDouble("price"),
                DishCategory.valueOf(rs.getString("category")),
                rs.getInt("preparation_time_minutes"),
                loadIngredientsForDish(id)
        );
    }
}
