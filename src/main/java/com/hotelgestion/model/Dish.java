package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Dish {
    private int id;
    private String name;
    private double price;
    private DishCategory category;
    private int preparationTimeMinutes;
    private Map<Ingredient, Double> requiredIngredients;

    public boolean isAvailable() {
        return requiredIngredients.entrySet().stream()
                .allMatch(entry -> {
                            var ingredient = entry.getKey();
                            var requiredQty = entry.getValue();
                            double availableQty = ingredient.getStockQuantity();
                            return availableQty >= requiredQty;
                        });
    }
}
