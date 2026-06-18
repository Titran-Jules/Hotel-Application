package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
public class StandardRoom extends Room {
    @Override
    public double calculateActualPrice() {
        return this.getBasePrice();
    }
}
