package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class OrderLine {
    private int id;
    private Dish dish;
    private int quantity;
    private double unitPrice;

    public double calculateSubtotal() {
        return this.getUnitPrice() * this.getQuantity();
    }
}
