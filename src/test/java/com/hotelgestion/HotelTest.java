package com.hotelgestion;

import com.hotelgestion.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HotelTest {
    private Amenity wifi;
    private Amenity jacouzi;
    private Amenity coffeeMachine;
    private Amenity airConditioning;
    private Amenity tV;

    private StandardRoom standardRoom1;
    private StandardRoom standardRoom2;
    private SuiteRoom suiteRoom1;
    private SuiteRoom suiteRoom2;

    private Cleaner cleaner1;
    private Cleaner cleaner2;
    private Guard guard1;
    private DayGuard dayGuard1;
    private Cook cook1;
    private Cook cook2;
    private Manager manager1;
    private Manager manager2;

    private Ingredient carot;
    private Ingredient sausage;
    private Ingredient tomato;
    private Ingredient cheese;
    private Ingredient flour;
    private Ingredient garlic;
    private Ingredient oliveOil;
    private Ingredient onion;
    private Ingredient milk;
    private Ingredient egg;
    private Ingredient pepper;
    private Ingredient salt;
    private Ingredient butter;
    private Ingredient mushroom;
    private Ingredient beef;
    private Ingredient rice;
    private Ingredient chicken;

    private Dish pizza;
    private Dish mushroomOmelette;
    private Dish roastedChicken;
    private Dish bologneseSauce;

    private Menu menuMainCourse;
    private Menu menuStarter;

    private Hotel hotel;

    private Guest guest1;
    private Guest guest2;
    private Guest guest3;
    private Guest guest4;

    @BeforeEach
    void setup() {
        wifi = new Amenity(1, "WI-FI", 10000);
        jacouzi = new Amenity(2, "Jacouzi", 25000);
        coffeeMachine = new Amenity(3, "Coffee Machine", 7000);
        airConditioning = new Amenity(4, "Air Conditioning", 10000);
        tV = new Amenity(5, "TV", 5000);

        standardRoom1 = StandardRoom.builder()
                .id(1)
                .roomNumber("001")
                .basePrice(20000)
                .bedCount(2)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(wifi, tV))
                .build();

        standardRoom2 = StandardRoom.builder()
                .id(2)
                .roomNumber("002")
                .basePrice(10000)
                .bedCount(1)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(tV))
                .build();

        suiteRoom1 = SuiteRoom.builder()
                .id(3)
                .roomNumber("003")
                .basePrice(60000)
                .bedCount(4)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(tV, wifi, jacouzi, coffeeMachine))
                .roomCount(4)
                .build();

        suiteRoom2 = SuiteRoom.builder()
                .id(4)
                .roomNumber("004")
                .basePrice(50000)
                .bedCount(4)
                .status(RoomStatus.AVAILABLE)
                .amenities(List.of(tV, wifi, coffeeMachine))
                .roomCount(4)
                .build();

        cleaner1 = Cleaner.builder()
                .id(1)
                .name("Rakoto")
                .phoneNumber("034 00 000 01")
                .salary(100000)
                .efficacity(60)
                .build();

        cleaner2 = Cleaner.builder()
                .id(2)
                .name("Josephine")
                .phoneNumber("033 00 000 02")
                .salary(100000)
                .efficacity(90)
                .build();

        guard1 = Guard.builder()
                .id(3)
                .name("Jean")
                .phoneNumber("034 01 001 01")
                .salary(110000)
                .patrolZone("The Hall")
                .dayGuards(new ArrayList<>())
                .bonusSalaryPerHour(5000)
                .build();

        dayGuard1 = DayGuard.builder()
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(22, 0))
                .shiftHour(false)
                .date(LocalDate.of(2026, 6, 23))
                .build();

        cook1 = Cook.builder()
                .id(3)
                .name("Luc")
                .phoneNumber("034 02 002 02")
                .salary(130000)
                .speciality("Dessert")
                .build();

        cook2 = Cook.builder()
                .id(4)
                .name("Julia")
                .phoneNumber("032 02 002 03")
                .salary(130000)
                .speciality("Main course")
                .build();

        manager1 = Manager.builder()
                .id(5)
                .name("Fanomezana")
                .phoneNumber("038 04 200 04")
                .salary(120000)
                .employees(List.of(cook1, cook2))
                .build();

        manager2 = Manager.builder()
                .id(6)
                .name("Fitiavana")
                .phoneNumber("038 05 004 03")
                .salary(120000)
                .employees(List.of(cleaner1, cleaner2, guard1))
                .build();

        carot = new Ingredient(1, "Carot", 50, "kg", 30);
        sausage = new Ingredient(2, "Sausage", 30, "kg", 30);
        tomato = new Ingredient(3, "tomato", 50, "pcs", 15);
        flour = new Ingredient(4, "flour", 25, "kg", 20);
        cheese = new Ingredient(5, "cheese", 10, "kg", 45);
        onion = new Ingredient(6, "onion", 15, "kg", 12);
        garlic = new Ingredient(7, "garlic", 5, "kg", 8);
        oliveOil = new Ingredient(8, "olive oil", 20, "L", 60);
        milk = new Ingredient(9, "milk", 40, "L", 35);
        egg = new Ingredient(10, "egg", 120, "pcs", 18);
        pepper = new Ingredient(11, "pepper", 2, "kg", 10);
        salt = new Ingredient(12, "salt", 10, "kg", 5);
        butter = new Ingredient(13, "butter", 15, "kg", 50);
        mushroom = new Ingredient(14, "mushroom", 8, "kg", 22);
        chicken = new Ingredient(15, "chicken", 35, "kg", 70);
        beef = new Ingredient(16, "beef", 25, "kg", 95);
        rice = new Ingredient(17, "rice", 50, "kg", 40);

        pizza = new Dish(1, "Royal Pizza", 20000, DishCategory.MAIN_COURSE, 20, Map.of(flour, 1d, tomato, 4d, cheese, 0.5d, sausage, 0.3d, mushroom, 0.2d));
        roastedChicken = new Dish(2, "Roasted Chicken & Rice", 25000, DishCategory.MAIN_COURSE, 45, Map.of(chicken, 1.5d, rice, 0.4d, onion, 2d, garlic, 3d, butter, 0.05d));
        mushroomOmelette = new Dish(3, "Mushroom Omelette", 12000, DishCategory.STARTER, 15, Map.of(egg, 6d, mushroom, 0.15d, butter, 0.02d, salt, 0.005d, pepper, 0.002d));
        bologneseSauce = new Dish(4, "Bolognese Sauce", 18000, DishCategory.MAIN_COURSE, 30, Map.of(beef, 0.5d, tomato, 6d, onion, 2d, garlic, 2d, oliveOil, 0.05d));

        menuMainCourse = new Menu(1, "Main Course", new ArrayList<>());
        menuStarter = new Menu(2, "Starter", new ArrayList<>());

        hotel = new Hotel(1, "SingleTon Hotel", "Ivandry", List.of(standardRoom1, standardRoom2, suiteRoom1, suiteRoom2), List.of(cleaner1, cleaner2, guard1, cook1, cook2, manager1, manager2));

        guest1 = new Guest(1, "Rakotosolofo jean patrick", "034 50 404 22", "solofojean@gmail.com");
        guest2 = new Guest(2, "Andriamandresy Lisa Princia", "038 00 230 44", "lisaprincia@gmail.com");
        guest3 = new Guest(3, "Bourdier LeGrand", "032 44 322 99", "legrand@gmail.com");
        guest4 = new Guest(4, "Rabearivony Sahinnah", "034 88 543 81", "sahinna@gmail.com");
    }

    // Titran's test
    @Test
    void test_calculateActualPrice_ok()  {
        assertEquals(35000, standardRoom1.calculateActualPrice());
        assertEquals(15000, standardRoom2.calculateActualPrice());
        assertEquals(187000, suiteRoom1.calculateActualPrice());
    }

    @Test
    void test_restaurantOrder_totalCost() {
        var orderLine1 = new OrderLine(1, pizza, 2, pizza.getPrice());
        var orderLine2 = new OrderLine(2, bologneseSauce, 2, bologneseSauce.getPrice());

        var restaurantOrder1 = new RestaurantOrder(1, guest1, standardRoom1, cook1, List.of(orderLine1, orderLine2), OrderStatus.IN_PREPARATION, LocalDateTime.now());
        assertEquals(76000, restaurantOrder1.calculateTotal());
    }

    @Test
    void test_add_and_remove_dish_ok() {
        assertEquals(0, menuMainCourse.getDishes().size());

        menuMainCourse.addDish(bologneseSauce);
        menuMainCourse.addDish(roastedChicken);
        menuMainCourse.addDish(mushroomOmelette);
        menuMainCourse.addDish(pizza);
        assertEquals(4, menuMainCourse.getDishes().size());

        menuMainCourse.removeDish(mushroomOmelette);
        assertEquals(3, menuMainCourse.getDishes().size());
    }
    // end Titran's test

    // Toky's test

    // end Toky's test

    // Manda's test

    // end Manda's test
}
