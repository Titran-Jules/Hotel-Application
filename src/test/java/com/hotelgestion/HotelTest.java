package com.hotelgestion;

import com.hotelgestion.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelTest {
    @BeforeEach
    void setup() {
        var wifi = new Amenity(1, "WI-FI", 10000);
        var jacouzi = new Amenity(2, "Jacouzi", 25000);
        var coffeeMachine = new Amenity(3, "Coffee Machine", 7000);
        var airConditioning = new Amenity(4, "Air Conditioning", 10000);
        var tV = new Amenity(5, "TV", 5000);

        var standardRoom1 = StandardRoom.builder()
                .id(1)
                .roomNumber("001")
                .basePrice(20000)
                .bedCount(2)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(wifi, tV))
                .build();

        var standardRoom2 = StandardRoom.builder()
                .id(2)
                .roomNumber("002")
                .basePrice(10000)
                .bedCount(1)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(tV))
                .build();

        var suiteRoom1 = SuiteRoom.builder()
                .id(3)
                .roomNumber("003")
                .basePrice(60000)
                .bedCount(4)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(tV, wifi, jacouzi, coffeeMachine))
                .roomCount(4)
                .build();

        var suiteRoom2 = SuiteRoom.builder()
                .id(4)
                .roomNumber("004")
                .basePrice(50000)
                .bedCount(4)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(tV, wifi, coffeeMachine))
                .roomCount(4)
                .build();

        var cleaner1 = Cleaner.builder()
                .id(1)
                .name("Rakoto")
                .phoneNumber("034 00 000 01")
                .salary(100000)
                .efficacity(60)
                .build();
    }
}
