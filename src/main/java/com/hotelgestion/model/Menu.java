package com.hotelgestion.model;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Menu {
    private int id;
    private String name;
    private List<Dish> dishes;
    private int hotelId;

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
