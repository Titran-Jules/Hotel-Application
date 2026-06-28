package com.hotelgestion.dao;

import com.hotelgestion.model.Dish;
import com.hotelgestion.model.Menu;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuDAO implements GenericDAO<Menu, Integer> {

    private final DishDAO dishDAO = new DishDAO();

    @Override
    public Menu create(Menu menu) {
        String sql = "INSERT INTO menu (name, hotel_id) VALUES (?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, menu.getName());
            stmt.setInt(2, menu.getHotelId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    menu.setId(rs.getInt("id"));
                }
            }
            saveMenuDishes(menu);
            return menu;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du menu", e);
        }
    }

    @Override
    public Optional<Menu> findById(Integer id) {
        String sql = "SELECT * FROM menu WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du menu " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        String sql = "SELECT * FROM menu";
        List<Menu> menus = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                menus.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les menus", e);
        }
        return menus;
    }

    @Override
    public boolean update(Menu menu) {
        String sql = "UPDATE menu SET name = ?, hotel_id = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, menu.getName());
            stmt.setInt(2, menu.getHotelId());
            stmt.setInt(3, menu.getId());

            boolean updated = stmt.executeUpdate() > 0;
            if (updated) {
                String deleteJoin = "DELETE FROM menu_dish WHERE menu_id = ?";
                try (PreparedStatement delStmt = conn.prepareStatement(deleteJoin)) {
                    delStmt.setInt(1, menu.getId());
                    delStmt.executeUpdate();
                }
                saveMenuDishes(menu);
            }
            return updated;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du menu " + menu.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM menu WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du menu " + id, e);
        }
    }

    public List<Dish> findMenuDishes(int menuId) {
        String sql = "SELECT dish_id FROM menu_dish WHERE menu_id = ?";
        List<Dish> dishes = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dishDAO.findById(rs.getInt("dish_id")).ifPresent(dishes::add);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des plats du menu " + menuId, e);
        }
        return dishes;
    }

    private void saveMenuDishes(Menu menu) throws SQLException {
        if (menu.getDishes() == null || menu.getDishes().isEmpty()) return;

        String sql = "INSERT INTO menu_dish (menu_id, dish_id) VALUES (?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Dish dish : menu.getDishes()) {
                stmt.setInt(1, menu.getId());
                stmt.setInt(2, dish.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private Menu mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");

        var menu = new Menu();
        menu.setId(id);
        menu.setName(rs.getString("name"));
        menu.setHotelId(rs.getInt("hotel_id"));

        menu.setDishes(findMenuDishes(id));

        return menu;
    }
}
