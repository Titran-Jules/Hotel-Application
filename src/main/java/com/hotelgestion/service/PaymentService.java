package com.hotelgestion.service;

import com.hotelgestion.dao.InvoiceDAO;
import com.hotelgestion.dao.PaymentDAO;
import com.hotelgestion.dao.ReservationDAO;
import com.hotelgestion.exception.PaymentFailedException;
import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PaymentService {
    private final PaymentDAO paymentDAO;
    private final InvoiceDAO invoiceDAO;
    private final ReservationDAO reservationDAO;

    public PaymentService(PaymentDAO paymentDAO, InvoiceDAO invoiceDAO, ReservationDAO reservationDAO) {
        this.paymentDAO = paymentDAO;
        this.invoiceDAO = invoiceDAO;
        this.reservationDAO = reservationDAO;
    }

    public Payment processPayment(Reservation r, Double amount, PaymentMethode method) {
        var payment = new Payment(0, r, r.getTotalPrice(), method, PaymentStatus.PENDING, LocalDateTime.now());

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                payment.validate();
                paymentDAO.update(payment);

                var invoice = new Invoice(0, r, new ArrayList<>(), LocalDateTime.now(), r.getTotalPrice());
                var mainLine = new InvoiceLine(0, "Séjour Chambre N° "+ r.getRoom().getRoomNumber() + " (" + r.calculateNumberOfNights() + " nuit(s)", r.getTotalPrice());
                invoice.addLine(mainLine);

                invoiceDAO.create(invoice);

                r.confirm();
                reservationDAO.update(r);

                conn.commit();
                return payment;
            } catch (Exception e) {
                conn.rollback();
                payment.fail();
                throw new PaymentFailedException("Le processus de paiement a échoué. Transaction annulée.");
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur d'accès à la base de donnée", e);
        }
    }
}
