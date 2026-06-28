package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAO implements GenericDAO<Reservation, Integer> {

    private final RoomDAO roomDAO = new RoomDAO();
    private final GuestDAO guestDAO = new GuestDAO();

    @Override
    public Reservation create(Reservation res) {
        String sql = """
                INSERT INTO reservation (guest_id, room_id, start_date, end_date, status, total_price)
                VALUES (?, ?, ?, ?, ?, ?) RETURNING id
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, res.getGuest().getId());
            stmt.setInt(2, res.getRoom().getId());
            stmt.setDate(3, Date.valueOf(res.getStartDate()));
            stmt.setDate(4, Date.valueOf(res.getEndDate()));
            stmt.setString(5, res.getStatus().name());
            stmt.setDouble(6, res.getTotalPrice());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    res.setId(rs.getInt("id"));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la réservation", e);
        }
    }

    @Override
    public Optional<Reservation> findById(Integer id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la réservation " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        List<Reservation> reservations = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les réservations", e);
        }
        return reservations;
    }

    @Override
    public boolean update(Reservation res) {
        String sql = """
                UPDATE reservation SET guest_id = ?, room_id = ?, start_date = ?, 
                                       end_date = ?, status = ?, total_price = ? WHERE id = ?
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, res.getGuest().getId());
            stmt.setInt(2, res.getRoom().getId());
            stmt.setDate(3, Date.valueOf(res.getStartDate()));
            stmt.setDate(4, Date.valueOf(res.getEndDate()));
            stmt.setString(5, res.getStatus().name());
            stmt.setDouble(6, res.getTotalPrice());
            stmt.setInt(7, res.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la réservation " + res.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la réservation " + id, e);
        }
    }

    public List<Reservation> findByGuest(int guestId) {
        String sql = "SELECT * FROM reservation WHERE guest_id = ?";
        List<Reservation> reservations = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des réservations du client " + guestId, e);
        }
        return reservations;
    }

    public List<Reservation> findByRoomAndPeriod(int roomId, LocalDate start, LocalDate end) {
        String sql = """
                SELECT * FROM reservation 
                WHERE room_id = ? 
                  AND status != 'CANCELLED'
                  AND start_date < ? 
                  AND end_date > ?
                """;
        List<Reservation> overlaps = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(end));
            stmt.setDate(3, Date.valueOf(start));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    overlaps.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification des chevauchements de réservation", e);
        }
        return overlaps;
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        var room = roomDAO.findById(rs.getInt("room_id"))
                .orElseThrow(() -> new RuntimeException("Chambre introuvable pour la réservation"));

        var guest = guestDAO.findById(rs.getInt("guest_id"))
                .orElseThrow(() -> new RuntimeException("Client introuvable pour la réservation"));

        return new Reservation(
                rs.getInt("id"),
                guest,
                room,
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                ReservationStatus.valueOf(rs.getString("status")),
                rs.getDouble("total_price")
        );
    }

    public List<Reservation> findByStatus(ReservationStatus reservationStatus) {
        String sql = "SELECT * FROM reservation WHERE status = ?";
        List<Reservation> reservations = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reservationStatus.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des réservations avec le statut " + reservationStatus, e);
        }
        return reservations;
    }
}
