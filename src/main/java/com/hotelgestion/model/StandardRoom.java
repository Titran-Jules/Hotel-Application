package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class StandardRoom extends Room {
    @Override
    public double calculateActualPrice() {
        return this.getBasePrice() + this.amenityTotalCost();
    }
}
