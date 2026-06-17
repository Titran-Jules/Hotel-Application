package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SuiteRoom extends Room {
    private int roomCount;


    @Override
    public double calculateActualPrice() {
        return (this.getBasePrice() + this.getRoomCount() * 20000) * (1 + TAX_RATE);
    }
}
