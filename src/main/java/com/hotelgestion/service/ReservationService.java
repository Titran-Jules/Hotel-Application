package com.hotelgestion.service;

import com.hotelgestion.dao.ReservationDAO;
import com.hotelgestion.dao.RoomDAO;
import com.hotelgestion.exception.RoomUnavailableException;
import com.hotelgestion.model.Reservation;
import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;

    public ReservationService(ReservationDAO reservationDAO, RoomDAO roomDAO) {
        this.reservationDAO = reservationDAO;
        this.roomDAO = roomDAO;
    }

    public Reservation createReservation(Guest guest, Room room, LocalDate startDate, LocalDate endDate) {
        boolean overlap = !reservationDAO.findByRoomAndPeriod(room.getId(), startDate, endDate).isEmpty();
        if (overlap || !room.isAvailable()) {
            throw new RoomUnavailableException("La chambre "+ room.getRoomNumber() + " n'est pas disponible du " + startDate + " au " + endDate);
        }

        var reservation = guest.book(room, startDate, endDate);
        reservation.setTotalPrice(reservation.calculateTotalPrice());

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                reservationDAO.create(reservation);
                room.changesStatus(RoomStatus.OCCUPIED);
                roomDAO.update(room);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur transactionnelle lors de la réservation", e);
        }

        return reservation;
    }

    public void validateReservation(int reservationId, Manager manager) {
        var reservation = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable : "+ reservationId));

        boolean validated = manager.validateReservation(reservation);
        if (validated) {
            reservation.confirm();
            reservationDAO.update(reservation);
        }
    }

    public void cancelReservation(int reservationId) {
        var reservation = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvble : "+ reservationId));

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                reservation.cancel();
                reservationDAO.update(reservation);

                Room room = reservation.getRoom();
                room.changesStatus(RoomStatus.AVAILABLE);
                roomDAO.update(room);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw  e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur transactionnelle lors de l'annulation", e);
        }
    }
}
