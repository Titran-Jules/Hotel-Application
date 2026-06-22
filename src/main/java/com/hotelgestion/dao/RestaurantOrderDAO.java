package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantOrderDAO implements GenericDAO<RestaurantOrder, Integer> {

    private final DishDAO dishDAO = new DishDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final GuestDAO guestDAO = new GuestDAO();

    @Override
    public RestaurantOrder create(RestaurantOrder order) {
        String sql = "INSERT INTO restaurant_order (guest_id, room_id, cook_id, status, order_date) VALUES (?, ?, ?, ?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getGuest().getId());
            if (order.getRoom() != null) stmt.setInt(2, order.getRoom().getId()); else stmt.setNull(2, Types.INTEGER);
            if (order.getCook() != null) stmt.setInt(3, order.getCook().getId()); else stmt.setNull(3, Types.INTEGER);
            stmt.setString(4, order.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(order.getOrderDate() != null ? order.getOrderDate() : LocalDateTime.now()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) order.setId(rs.getInt("id"));
            }
            saveOrderLines(order);
            return order;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création commande", e);
        }
    }

    @Override
    public Optional<RestaurantOrder> findById(Integer id) {
        String sql = "SELECT * FROM restaurant_order WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById commande", e);
        }
        return Optional.empty();
    }

    @Override
    public List<RestaurantOrder> findAll() {
        String sql = "SELECT * FROM restaurant_order";
        List<RestaurantOrder> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll commandes", e);
        }
        return list;
    }

    @Override
    public boolean update(RestaurantOrder order) {
        String sql = "UPDATE restaurant_order SET guest_id = ?, room_id = ?, cook_id = ?, status = ?, order_date = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getGuest().getId());
            if (order.getRoom() != null) stmt.setInt(2, order.getRoom().getId()); else stmt.setNull(2, Types.INTEGER);
            if (order.getCook() != null) stmt.setInt(3, order.getCook().getId()); else stmt.setNull(3, Types.INTEGER);
            stmt.setString(4, order.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(order.getOrderDate()));
            stmt.setInt(6, order.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update commande", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM restaurant_order WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete commande", e);
        }
    }

    public List<RestaurantOrder> findByStatus(OrderStatus status) {
        String sql = "SELECT * FROM restaurant_order WHERE status = ?";
        List<RestaurantOrder> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByStatus commandes", e);
        }
        return list;
    }

    public List<RestaurantOrder> findByGuest(int guestId) {
        String sql = "SELECT * FROM restaurant_order WHERE guest_id = ?";
        List<RestaurantOrder> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByGuest commandes", e);
        }
        return list;
    }

    private void saveOrderLines(RestaurantOrder order) throws SQLException {
        if (order.getLines() == null) return;
        String sql = "INSERT INTO order_line (order_id, dish_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (OrderLine line : order.getLines()) {
                stmt.setInt(1, order.getId());
                stmt.setInt(2, line.getDish().getId());
                stmt.setInt(3, line.getQuantity());
                stmt.setDouble(4, line.getUnitPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private List<OrderLine> loadOrderLines(int orderId) {
        String sql = "SELECT * FROM order_line WHERE order_id = ?";
        List<OrderLine> lines = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dish dish = dishDAO.findById(rs.getInt("dish_id")).orElse(null);
                    lines.add(new OrderLine(
                            rs.getInt("id"),
                            dish,
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement lignes commande " + orderId, e);
        }
        return lines;
    }

    private RestaurantOrder mapRow(ResultSet rs) throws SQLException {
        int guestId = rs.getInt("guest_id");
        Guest guest = guestDAO.findById(guestId).orElse(null);
        int roomId = rs.getInt("room_id");
        Room room = rs.wasNull() ? null : roomDAO.findById(roomId).orElse(null);

        int cookId = rs.getInt("cook_id");
        Cook cook = rs.wasNull() ? null : (Cook) employeeDAO.findById(cookId).orElse(null);

        int orderId = rs.getInt("id");
        return new RestaurantOrder(
                orderId,
                guest,
                room,
                cook,
                loadOrderLines(orderId),
                OrderStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("order_date").toLocalDateTime()
        );
    }
}
