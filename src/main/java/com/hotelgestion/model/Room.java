package com.hotelgestion.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.naming.InsufficientResourcesException;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public abstract class Room {
    private int id;
    private String roomNumber;
    private double basePrice;
    private int bedCount;
    private RoomStatus status;
    private List<Amenity> amenities;

    protected static final double TAX_RATE = 0.20;

    public abstract double calculateActualPrice();

    public void addAmenity(Amenity a) {
        amenities.add(a);
    }

    public void removeAmenity(Amenity a) {
        amenities.remove(a);
    }

    public double amenityTotalCost() {
        if (!amenities.isEmpty()) {
            return amenities.stream()
                .mapToDouble(Amenity::getAdditionalCost)
                .sum();
        } else {
            return 0;
        }
    }

    public boolean isAvailable() {
        return this.status.equals(RoomStatus.AVAILABLE);
    }

    public void changesStatus(RoomStatus s) {
        this.status = s;
    }
}
