package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Ingredient {
    private int id;
    private String name;
    private double stockQuantity;
    private String unit;
    private double alertThreshold;

    public void decreaseStock(double qty) {
        if (stockQuantity >= qty) {
            stockQuantity -= qty;
        }
    }

    public void increaseStock(double qty) {
        stockQuantity += qty;
    }

    public boolean isLowStock() {
        return stockQuantity <= alertThreshold;
    }
}
