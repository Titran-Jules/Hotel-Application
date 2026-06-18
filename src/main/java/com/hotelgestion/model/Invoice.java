package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Invoice {
    private int id;
    private Reservation reservation;
    private List<InvoiceLine> lines;
    private LocalDateTime issueDate;
    private double totalAmount;

    public void addLine(InvoiceLine l) {
        this.lines.add(l);
    }

    public double calculateTotal() {
        return lines.stream()
                .mapToDouble(InvoiceLine::getAmount)
                .sum();
    }

    public String generateSummary() {
        return lines.stream()
                .map(InvoiceLine::getDescription)
                .collect(Collectors.joining(" - "));
    }
}
