package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Menu {
    private int id;
    private String name;
    private List<Dish> dishes;

    public void addDish(Dish d) {
        dishes.add(d);
    }

    public void removeDish(Dish d) {
        dishes.remove(d);
    }

    public List<Dish> filterByCategory(DishCategory c) {
        return dishes.stream()
                .filter(d -> d.getCategory().equals(c))
                .collect(Collectors.toList());
    }
}
