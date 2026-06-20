package com.hotelgestion.dao;

import com.hotelgestion.model.Invoice;
import com.hotelgestion.model.InvoiceLine;
import com.hotelgestion.model.Reservation;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDAO implements GenericDAO<Invoice, Integer> {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    public Invoice create(Invoice invoice) {
        String sql = "INSERT INTO invoice (reservation_id, issue_date, total_amount) VALUES (?, ?, ?) RETURNING id";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoice.getReservation().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(invoice.getIssueDate() != null ? invoice.getIssueDate() : LocalDateTime.now()));
            stmt.setDouble(3, invoice.getTotalAmount());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) invoice.setId(rs.getInt("id"));
            }
            saveInvoiceLines(invoice);
            return invoice;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la facture", e);
        }
    }

    @Override
    public Optional<Invoice> findById(Integer id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById facture " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Invoice> findAll() {
        String sql = "SELECT * FROM invoice";
        List<Invoice> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll factures", e);
        }
        return list;
    }

    @Override
    public boolean update(Invoice invoice) {
        String sql = "UPDATE invoice SET reservation_id = ?, issue_date = ?, total_amount = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoice.getReservation().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(invoice.getIssueDate()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setInt(4, invoice.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update facture", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM invoice WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete facture", e);
        }
    }

    public List<Invoice> findByReservation(int reservationId) {
        String sql = "SELECT * FROM invoice WHERE reservation_id = ?";
        List<Invoice> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByReservation factures", e);
        }
        return list;
    }

    private void saveInvoiceLines(Invoice invoice) throws SQLException {
        if (invoice.getLines() == null) return;
        String sql = "INSERT INTO invoice_line (invoice_id, description, amount) VALUES (?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (InvoiceLine line : invoice.getLines()) {
                stmt.setInt(1, invoice.getId());
                stmt.setString(2, line.getDescription());
                stmt.setDouble(3, line.getAmount());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private List<InvoiceLine> loadInvoiceLines(int invoiceId) {
        String sql = "SELECT * FROM invoice_line WHERE invoice_id = ?";
        List<InvoiceLine> lines = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lines.add(new InvoiceLine(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getDouble("amount")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement lignes facture " + invoiceId, e);
        }
        return lines;
    }

    private Invoice mapRow(ResultSet rs) throws SQLException {
        Reservation res = reservationDAO.findById(rs.getInt("reservation_id")).orElse(null);
        int invoiceId = rs.getInt("id");
        return new Invoice(
                invoiceId,
                res,
                loadInvoiceLines(invoiceId),
                rs.getTimestamp("issue_date").toLocalDateTime(),
                rs.getDouble("total_amount")
        );
    }
}
