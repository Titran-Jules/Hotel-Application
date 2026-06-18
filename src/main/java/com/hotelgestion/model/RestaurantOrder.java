package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RestaurantOrder {
    private int id;
    private Guest guest;
    private Room room;
    private Cook cook;
    private List<OrderLine> lines;
    private OrderStatus status;
    private LocalDateTime orderDate;

    public void addLine(Dish d, int quantity) {
        if (quantity <= 0) return;

        for (OrderLine line : lines) {
            if (line.getDish().equals(d)) {
                line.setQuantity(line.getQuantity() + quantity);
                return;
            }
        }

        int nextId = this.lines.size() + 1;
        lines.add(new OrderLine(nextId, d, quantity, d.getPrice()));
    }

    public double calculateTotal() {
        return lines.stream()
                .mapToDouble(OrderLine::calculateSubtotal)
                .sum();
    }

    public void startPreparation() {
        this.status = OrderStatus.IN_PREPARATION;
    }

    public void markAsReady() {
        if (this.status.equals(OrderStatus.IN_PREPARATION)) {
            this.status = OrderStatus.READY;
        }
    }

    public void markAsDelivered() {
        if (this.status.equals(OrderStatus.READY)) {
            this.status = OrderStatus.DELIVERED;
        }
    }

    public void markAsCancelled() {
        this.status = OrderStatus.CANCELED;
    }
}
