package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Amenity {
    private int id;
    private String name;
    private double additionalCost;
}
