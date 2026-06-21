package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDAO implements GenericDAO<Payment, Integer> {

    @Override
    public Payment create(Payment payment) {
        String sql = """
                INSERT INTO payment (reservation_id, amount, payment_method, status, payment_date)
                VALUES (?, ?, ?, ?, ?) RETURNING id
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getReservation().getId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethode().name());
            stmt.setString(4, payment.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate() != null ? payment.getPaymentDate() : LocalDateTime.now()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payment.setId(rs.getInt("id"));
                }
            }
            return payment;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du paiement", e);
        }
    }

    @Override
    public Optional<Payment> findById(Integer id) {
        String sql = "SELECT * FROM payment WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du paiement " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM payment";
        List<Payment> payments = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                payments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les paiements", e);
        }
        return payments;
    }

    @Override
    public boolean update(Payment payment) {
        String sql = """
                UPDATE payment SET reservation_id = ?, amount = ?, payment_method = ?, 
                                   status = ?, payment_date = ? WHERE id = ?
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getReservation().getId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethode().name());
            stmt.setString(4, payment.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setInt(6, payment.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du paiement " + payment.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM payment WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du paiement " + id, e);
        }
    }

    public List<Payment> findByReservation(int reservationId) {
        String sql = "SELECT * FROM payment WHERE reservation_id = ?";
        List<Payment> payments = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage des paiements pour la réservation " + reservationId, e);
        }
        return payments;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation(
                rs.getInt("reservation_id"),
                null, null, null, null, null, 0.0
        );

        String dbMethod = rs.getString("payment_method");
        PaymentMethode method;
        try {
            method = PaymentMethode.valueOf(dbMethod);
        } catch (IllegalArgumentException e) {
            method = PaymentMethode.CASH;
        }

        return new Payment(
                rs.getInt("id"),
                reservation,
                rs.getDouble("amount"),
                method,
                PaymentStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("payment_date").toLocalDateTime()
        );
    }
}
