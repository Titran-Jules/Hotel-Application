package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StandardRoom extends Room {
    @Override
    public double getRealPrice() {
        return this.getBasePrice();
    }
}
