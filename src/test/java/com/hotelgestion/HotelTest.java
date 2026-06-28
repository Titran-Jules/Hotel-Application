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
    private Guard guard2;
    private DayGuard dayGuard1;
    private DayGuard dayGuard2;
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
                .amenities(new ArrayList<>(List.of(wifi, tV)))
                .build();

        standardRoom2 = StandardRoom.builder()
                .id(2)
                .roomNumber("002")
                .basePrice(10000)
                .bedCount(1)
                .status(RoomStatus.AVAILABLE)
                .amenities(new ArrayList<>(List.of(tV)))
                .build();

        suiteRoom1 = SuiteRoom.builder()
                .id(3)
                .roomNumber("003")
                .basePrice(60000)
                .bedCount(4)
                .status(RoomStatus.AVAILABLE)
                .amenities(new ArrayList<>(List.of(tV, wifi, jacouzi, coffeeMachine)))
                .roomCount(4)
                .build();

        suiteRoom2 = SuiteRoom.builder()
                .id(4)
                .roomNumber("004")
                .basePrice(50000)
                .bedCount(4)
                .status(RoomStatus.AVAILABLE)
                .amenities(new ArrayList<>(List.of(tV, wifi, coffeeMachine)))
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


        guard2 = Guard.builder()
                .id(4)
                .name("John")
                .phoneNumber("034 11 011 01")
                .salary(120000)
                .patrolZone("The Gate")
                .dayGuards(new ArrayList<>())
                .bonusSalaryPerHour(10000)
                .build();

        dayGuard1 = DayGuard.builder()
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(22, 0))
                .shiftHour(false)
                .date(LocalDate.of(2026, 6, 23))
                .build();

        dayGuard2 = DayGuard.builder()
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(22, 0))
                .shiftHour(true)
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
                .employees(new ArrayList<>(List.of(cook1, cook2)))
                .build();

        manager2 = Manager.builder()
                .id(6)
                .name("Fitiavana")
                .phoneNumber("038 05 004 03")
                .salary(120000)
                .employees(new ArrayList<>(List.of(cleaner1,cleaner2, guard1)))
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

        hotel = new Hotel(1, "SingleTon Hotel", "Ivandry", List.of(standardRoom1, standardRoom2, suiteRoom1, suiteRoom2), List.of(cleaner1, cleaner2, guard1, cook1, cook2, manager1, manager2));

        guest1 = new Guest(1, "Rakotosolofo jean patrick", "034 50 404 22", "solofojean@gmail.com");
        guest2 = new Guest(2, "Andriamandresy Lisa Princia", "038 00 230 44", "lisaprincia@gmail.com");
        guest3 = new Guest(3, "Bourdier LeGrand", "032 44 322 99", "legrand@gmail.com");
        guest4 = new Guest(4, "Rabearivony Sahinnah", "034 88 543 81", "sahinna@gmail.com");
    }

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
    void test_calculate_Real_Salary_should_Return_Base_Salary() {
        assertEquals(110000, guard1.calculateRealSalary(), 0.0001);
        assertEquals(120000, guard2.calculateRealSalary(), 0.0001);
    }

    @Test
    void test_calculate_Real_Salary_should_Return_Base_Salary_when_No_Shift_Hours() {
        guard1.getDayGuards().add(dayGuard1);
        guard2.getDayGuards().add(dayGuard2);
        assertEquals(110000, guard1.calculateRealSalary(), 0.0001);
        assertNotEquals(120000, guard2.calculateRealSalary(), 0.0);
    }

    @Test
    void test_calculate_Real_Salary_should_Add_Bonus_when_Shift_Hours_Present() {
        guard2.getDayGuards().add(dayGuard2);
        guard1.getDayGuards().add(dayGuard1);


        assertEquals(120000 + 12 * 10000, guard2.calculateRealSalary(), 0.0001);
    }

    @Test
    void calculate_Real_Salary_should_Return_Salary_Plus_Ten_Percent() {
        assertEquals(130000 * 1.10, cook1.calculateRealSalary(), 0.0001);
    }

    @Test
    void calculate_Real_Salary_should_Be_The_Same_for_Both_Cooks() {
        assertEquals(cook1.calculateRealSalary(), cook2.calculateRealSalary(), 0.0001);
    }

    @Test
    void get_Speciality_should_Return_Correct_Speciality() {
        assertEquals("Dessert", cook1.getSpeciality());
        assertEquals("Main course", cook2.getSpeciality());
    }

    @Test
    void calculate_Real_Salary_should_Return_Salary_MultipliedBy_Efficacity() {
        assertEquals(100000 * 60, cleaner1.calculateRealSalary(), 0.0001);
    }

    @Test
    void calculate_Real_Salary_should_Return_Higher_Salary_when_Higher_Efficacity() {
        assertTrue(cleaner2.calculateRealSalary() > cleaner1.calculateRealSalary());
    }

    @Test
    void calculate_Real_Salary_should_Scale_With_Team_Size() {
        assertTrue(manager2.calculateRealSalary() > manager1.calculateRealSalary());
    }

    @Test
    void give_Order_should_Return_Expected_Message() {
        assertEquals("I need you to clean the room buddy", manager1.giveOrder());
    }

    @Test
    void add_Team_Member_should_Increase_Team_Size() {
        int sizeBefore = manager1.getEmployees().size();
        manager1.addTeamMember(cleaner1);
        assertEquals(sizeBefore + 1, manager1.getEmployees().size());
    }

    @Test
    void removeTeamMember_shouldDecreaseTeamSize() {
        int sizeBefore = manager2.getEmployees().size();
        manager2.removeTeamMember(cleaner1);
        assertEquals(sizeBefore - 1, manager2.getEmployees().size());
    }

    @Test
    void calculate_Actual_Price_with_Two_Amenities() {
        assertEquals(35000.0, standardRoom1.calculateActualPrice(), 0.01);
    }

    @Test
    void calculate_Actual_Price_with_One_Amenity() {
        assertEquals(15000.0, standardRoom2.calculateActualPrice(), 0.01);
    }

    @Test
    void calculate_Actual_Price_no_Amenities() {
        standardRoom1.getAmenities().clear();
        assertEquals(20000.0, standardRoom1.calculateActualPrice(), 0.01);
    }

    @Test
    void amenity_Total_Cost_two_Amenities() {
        assertEquals(15000.0, standardRoom1.amenityTotalCost(), 0.01);
    }

    @Test
    void amenity_Total_Cost_one_Amenity() {
        assertEquals(5000.0, standardRoom2.amenityTotalCost(), 0.01);
    }

    @Test
    void amenity_Total_Cost_empty_List() {
        standardRoom1.getAmenities().clear();
        assertEquals(0.0, standardRoom1.amenityTotalCost(), 0.01);
    }

    @Test
    void add_Amenity_increases_Size() {
        var airConditioning = new Amenity(4, "Air Conditioning", 10000);
        standardRoom1.addAmenity(airConditioning);

        assertTrue(standardRoom1.getAmenities().contains(airConditioning));
        assertEquals(3, standardRoom1.getAmenities().size());
    }

    @Test
    void remove_Amenity_decreases_Size() {
        standardRoom1.removeAmenity(wifi);

        assertFalse(standardRoom1.getAmenities().contains(wifi));
        assertEquals(1, standardRoom1.getAmenities().size());
    }

    @Test
    void is_Available_when_Available() {
        assertTrue(standardRoom1.isAvailable());
    }

    @Test
    void is_Available_when_Occupied() {
        standardRoom1.changesStatus(RoomStatus.OCCUPIED);
        assertFalse(standardRoom1.isAvailable());
    }

    @Test
    void changes_Status_updates_Status_standard_room() {
        standardRoom1.changesStatus(RoomStatus.CLEANING);
        assertEquals(RoomStatus.CLEANING, standardRoom1.getStatus());
    }



    @Test
    void calculate_Actual_Price_zero_Room_Count() {
        suiteRoom1.setRoomCount(0);
        assertEquals(107000.0, suiteRoom1.calculateActualPrice(), 0.01);
    }

    @Test
    void calculate_Actual_Price_noAmenities() {
        suiteRoom1.getAmenities().clear();
        assertEquals(140000.0, suiteRoom1.calculateActualPrice(), 0.01);
    }

    @Test
    void amenity_Total_Cost_four_Amenities() {
        assertEquals(47000.0, suiteRoom1.amenityTotalCost(), 0.01);
    }

    @Test
    void amenity_Total_Cost_three_Amenities() {
        assertEquals(22000.0, suiteRoom2.amenityTotalCost(), 0.01);
    }

    @Test
    void get_Room_Count_returns_Initial_Value() {
        assertEquals(4, suiteRoom1.getRoomCount());
    }

    @Test
    void set_Room_Count_updates_Value() {
        suiteRoom1.setRoomCount(6);
        assertEquals(6, suiteRoom1.getRoomCount());
    }

    @Test
    void is_Available_by_Default() {
        assertTrue(suiteRoom1.isAvailable());
    }

    @Test
    void is_Available_after_Out_Of_Service() {
        suiteRoom1.changesStatus(RoomStatus.OUT_OF_SERVICE);
        assertFalse(suiteRoom1.isAvailable());
    }

    @Test
    void changes_Status_updates_Status_suite_room() {
        suiteRoom2.changesStatus(RoomStatus.CLEANING);
        assertEquals(RoomStatus.CLEANING, suiteRoom2.getStatus());
    }

    @Test
    void book_shouldReturnReservationWithCorrectId() {
        var res = new Reservation(5, guest1, standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3), ReservationStatus.PENDING, 0.0);
        assertEquals(5, res.getId());
    }

    @Test
    void calculateActualPrice_standardRoom1_2nights() {
        var res = guest1.book(standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3));
        assertEquals(70000.0, res.getTotalPrice());
    }

    @Test
    void calculateTotalPrice_standardRoom1_2nights() {
        var res = guest1.book(standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3));
        assertEquals(70000.0, res.getTotalPrice());
    }

    @Test
    void calculateTotalPrice_suiteRoom1_2nights() {
        var res = guest1.book(suiteRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3));
        assertEquals(374000.0, res.getTotalPrice());
    }

    @Test
    void calculateNumberOfNights() {
        var res = new Reservation(0, guest1, standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5), ReservationStatus.PENDING, 0.0);
        assertEquals(4, res.calculateNumberOfNights());
    }

    @Test
    void book_shouldReturnReservationWithCorrectGuest() {
        var res = guest4.book(standardRoom2,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5));;
        assertEquals(guest4, res.getGuest());
    }

    @Test
    void book_shouldReturnReservationWithCorrectRoom() {
        var res = guest2.book(standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5));
        assertEquals(standardRoom1, res.getRoom());
    }

    @Test
    void confirm_shouldSetStatusToConfirmed_whenRoomAvailableAndDatesValid() {
        var res = new Reservation(1, guest1, standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                ReservationStatus.PENDING, 0.0);
        res.confirm();
        assertEquals(ReservationStatus.CONFIRMED, res.getStatus());
    }

    @Test
    void confirm_shouldSetRoomToOccupied_whenConfirmed() {
        var res = new Reservation(1, guest1, standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                ReservationStatus.PENDING, 0.0);
        res.confirm();
        assertEquals(RoomStatus.OCCUPIED, standardRoom1.getStatus());
    }

    @Test
    void confirm_shouldNotConfirm_whenRoomOccupied() {
        standardRoom1.changesStatus(RoomStatus.OCCUPIED);
        var res = new Reservation(1, guest1, standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                ReservationStatus.PENDING, 0.0);
        res.confirm();
        assertEquals(ReservationStatus.PENDING, res.getStatus());
    }


    @Test
    void confirm_shouldNotConfirm_whenEndDateBeforeStartDate() {
        var res = new Reservation(1, guest1, standardRoom1,
                LocalDate.of(2026, 7, 5),
                LocalDate.of(2026, 7, 1),
                ReservationStatus.PENDING, 0.0);
        res.confirm();
        assertEquals(ReservationStatus.PENDING, res.getStatus());
    }


    @Test
    void cancel_shouldSetRoomToAvailable_whenCancelled() {
        standardRoom1.changesStatus(RoomStatus.OCCUPIED);
        var res = new Reservation(1, guest1, standardRoom1,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                ReservationStatus.CONFIRMED, 0.0);
        res.cancel();
        assertEquals(RoomStatus.AVAILABLE, standardRoom1.getStatus());
    }



    @Test
    void validate_shouldSetStatusToValidated() {
        var payment = new Payment(1, null, 140000.0, PaymentMethode.CASH, PaymentStatus.PENDING, LocalDateTime.now());
        payment.validate();
        assertEquals(PaymentStatus.VALIDATED, payment.getStatus());
    }

    @Test
    void refund_shouldSetStatusToRefunded() {
        var payment = new Payment(1, null, 140000.0, PaymentMethode.CASH, PaymentStatus.VALIDATED, LocalDateTime.now());
        payment.refund();
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }

    @Test
    void fail_shouldSetStatusToFailed() {
        var payment = new Payment(1, null, 140000.0, PaymentMethode.CASH, PaymentStatus.PENDING, LocalDateTime.now());
        payment.fail();
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }
    @Test
    void testEquals_sameData_returnsTrue() {
        var payment = new Payment(1, null, 150.0, PaymentMethode.CARD, PaymentStatus.PENDING, null);
        var other = new Payment(1, null, 150.0, PaymentMethode.CARD, PaymentStatus.PENDING, null);
        assertEquals(payment, other);
    }
}
