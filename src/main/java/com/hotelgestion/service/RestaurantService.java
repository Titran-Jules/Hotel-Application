package com.hotelgestion.service;

import com.hotelgestion.dao.RestaurantOrderDAO;
import com.hotelgestion.dao.EmployeeDAO;
import com.hotelgestion.exception.InsufficientStockException;
import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantService {

    private final RestaurantOrderDAO restaurantOrderDAO;
    private final EmployeeDAO employeeDAO;

    public RestaurantService(RestaurantOrderDAO restaurantOrderDAO, EmployeeDAO employeeDAO) {
        this.restaurantOrderDAO = restaurantOrderDAO;
        this.employeeDAO = employeeDAO;
    }

    public RestaurantOrder placeOrder(Guest guest, Room roomOrNull, List<Dish> dishes, List<Integer> quantities) {
        if (dishes.size() != quantities.size()) {
            throw new IllegalArgumentException("La liste des plats et celle des quantités doivent avoir la même taille.");
        }

        RestaurantOrder order = new RestaurantOrder(
                0, guest, roomOrNull, null, new ArrayList<>(), OrderStatus.IN_PREPARATION, LocalDateTime.now()
        );

        for (int i = 0; i < dishes.size(); i++) {
            order.addLine(dishes.get(i), quantities.get(i));
        }

        Map<Ingredient, Double> totalRequiredIngredients = new HashMap<>();
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            int dishQuantity = quantities.get(i);

            for (Map.Entry<Ingredient, Double> entry : dish.getRequiredIngredients().entrySet()) {
                Ingredient ingredient = entry.getKey();
                double qtyPerDish = entry.getValue();
                double totalQtyForThisLine = qtyPerDish * dishQuantity;

                totalRequiredIngredients.put(
                        ingredient,
                        totalRequiredIngredients.getOrDefault(ingredient, 0.0) + totalQtyForThisLine
                );
            }
        }

        for (Map.Entry<Ingredient, Double> entry : totalRequiredIngredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            double totalRequired = entry.getValue();

            if (ingredient.getStockQuantity() < totalRequired) {
                throw new InsufficientStockException("Stock insuffisant pour l'ingrédient : " +
                        ingredient.getName() + " (Requis: " + totalRequired + ", Disponible: " + ingredient.getStockQuantity() + ")");
            }
        }

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                String updateIngredientSql = "UPDATE ingredient SET stock_quantity = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateIngredientSql)) {
                    for (Map.Entry<Ingredient, Double> entry : totalRequiredIngredients.entrySet()) {
                        Ingredient ingredient = entry.getKey();
                        double totalRequired = entry.getValue();

                        ingredient.decreaseStock(totalRequired);

                        stmt.setDouble(1, ingredient.getStockQuantity());
                        stmt.setInt(2, ingredient.getId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                restaurantOrderDAO.create(order);

                conn.commit();
                return order;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur critique BDD lors de la prise de commande au restaurant", e);
        }
    }

    public void assignCook(int orderId, Cook c) {
        RestaurantOrder order = restaurantOrderDAO.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id : " + orderId));

        employeeDAO.findById(c.getId())
                .orElseThrow(() -> new RuntimeException("Le cuisinier référencé n'existe pas en base de données."));

        order.setCook(c);
        order.startPreparation();

        restaurantOrderDAO.update(order);
    }

    public void completeOrder(int orderId) {
        RestaurantOrder order = restaurantOrderDAO.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id : " + orderId));

        order.markAsReady();
        order.markAsDelivered();

        restaurantOrderDAO.update(order);
    }
}
