package com.hotelgestion;

import com.hotelgestion.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        var cleaner2 = Cleaner.builder()
                .id(2)
                .name("Josephine")
                .phoneNumber("033 00 000 02")
                .salary(100000)
                .efficacity(90)
                .build();

        var guard1 = Guard.builder()
                .id(3)
                .name("Jean")
                .phoneNumber("034 01 001 01")
                .salary(110000)
                .patrolZone("The Hall")
                .dayGuards(new ArrayList<>())
                .bonusSalaryPerHour(5000)
                .build();

        var dayGuard1 = DayGuard.builder()
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(22, 0))
                .shiftHour(false)
                .date(LocalDate.of(2026, 6, 23))
                .build();

        var cook1 = Cook.builder()
                .id(3)
                .name("Luc")
                .phoneNumber("034 02 002 02")
                .salary(130000)
                .speciality("Dessert")
                .build();

        var cook2 = Cook.builder()
                .id(4)
                .name("Julia")
                .phoneNumber("032 02 002 03")
                .salary(130000)
                .speciality("Main course")
                .build();

        var manager1 = Manager.builder()
                .id(5)
                .name("Fanomezana")
                .phoneNumber("038 04 200 04")
                .salary(120000)
                .employees(List.of(cook1, cook2))
                .build();

        var manager2 = Manager.builder()
                .id(6)
                .name("Fitiavana")
                .phoneNumber("038 05 004 03")
                .salary(120000)
                .employees(List.of(cleaner1, cleaner2, guard1))
                .build();

        var carot = new Ingredient(1, "Carot", 50, "kg", 30);
        var sausage = new Ingredient(2, "Sausage", 30, "kg", 30);
        var tomato = new Ingredient(3, "tomato", 50, "pcs", 15);
        var flour = new Ingredient(4, "flour", 25, "kg", 20);
        var cheese = new Ingredient(5, "cheese", 10, "kg", 45);
        var onion = new Ingredient(6, "onion", 15, "kg", 12);
        var garlic = new Ingredient(7, "garlic", 5, "kg", 8);
        var oliveOil = new Ingredient(8, "olive oil", 20, "L", 60);
        var milk = new Ingredient(9, "milk", 40, "L", 35);
        var egg = new Ingredient(10, "egg", 120, "pcs", 18);
        var pepper = new Ingredient(11, "pepper", 2, "kg", 10);
        var salt = new Ingredient(12, "salt", 10, "kg", 5);
        var butter = new Ingredient(13, "butter", 15, "kg", 50);
        var mushroom = new Ingredient(14, "mushroom", 8, "kg", 22);
        var chicken = new Ingredient(15, "chicken", 35, "kg", 70);
        var beef = new Ingredient(16, "beef", 25, "kg", 95);
        var rice = new Ingredient(17, "rice", 50, "kg", 40);

        var pizza = new Dish(1, "Royal Pizza", 20000, DishCategory.MAIN_COURSE, 20, Map.of(flour, 1d, tomato, 4d, cheese, 0.5d, sausage, 0.3d, mushroom, 0.2d));
        var roastedChicken = new Dish(2, "Roasted Chicken & Rice", 25000, DishCategory.MAIN_COURSE, 45, Map.of(chicken, 1.5d, rice, 0.4d, onion, 2d, garlic, 3d, butter, 0.05d));
        var mushroomOmelette = new Dish(3, "Mushroom Omelette", 12000, DishCategory.STARTER, 15, Map.of(egg, 6d, mushroom, 0.15d, butter, 0.02d, salt, 0.005d, pepper, 0.002d));
        var bologneseSauce = new Dish(4, "Bolognese Sauce", 18000, DishCategory.MAIN_COURSE, 30, Map.of(beef, 0.5d, tomato, 6d, onion, 2d, garlic, 2d, oliveOil, 0.05d));

        var menuMainCourse = new Menu(1, "Main Course", List.of(pizza, roastedChicken, bologneseSauce));
        var menuStarter = new Menu(2, "Starter", List.of(mushroomOmelette));

        var hotel = new Hotel(1, "SingleTon Hotel", "Ivandry", List.of(standardRoom1, standardRoom2, suiteRoom1, suiteRoom2), List.of(cleaner1, cleaner2, guard1, cook1, cook2, manager1, manager2));
    }

    // Titran's test

    // end Titran's test

    // Toky's test

    // end Toky's test

    // Manda's test

    // end Manda's test
}
