package com.hotelgestion.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class Cook extends Employee {
    private String speciality;

    @Override
    public double calculateRealSalary() {
        return this.getSalary() * 1.10;
    }

    public void prepareDish(Dish d) {
        d.getRequiredIngredients().forEach((key, value) -> key.decreaseStock(value));
    }
}
