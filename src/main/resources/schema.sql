DROP TABLE IF EXISTS order_line CASCADE;
DROP TABLE IF EXISTS restaurant_order CASCADE;
DROP TABLE IF EXISTS dish_ingredient CASCADE;
DROP TABLE IF EXISTS menu_dish CASCADE;
DROP TABLE IF EXISTS menu CASCADE;
DROP TABLE IF EXISTS invoice_line CASCADE;
DROP TABLE IF EXISTS invoice CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS day_guard CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS room_amenity CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS dish CASCADE;
DROP TABLE IF EXISTS guest CASCADE;
DROP TABLE IF EXISTS amenity CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;

CREATE TABLE hotel (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       address VARCHAR(255) NOT NULL
);

CREATE TABLE amenity (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         additional_cost NUMERIC(10,2) DEFAULT 0 NOT NULL
);

CREATE TABLE guest (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       phone VARCHAR(30),
                       email VARCHAR(150) NOT NULL
);

CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            stock_quantity NUMERIC(10,2) DEFAULT 0 NOT NULL,
                            unit VARCHAR(20) NOT NULL,
                            alert_threshold NUMERIC(10,2) DEFAULT 0 NOT NULL
);

CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
                      category VARCHAR(20) NOT NULL CHECK (category IN ('APPETIZER', 'MAIN_COURSE', 'DESSERT', 'BEVERAGE')),
                      preparation_time_minutes INT DEFAULT 0 NOT NULL
);

CREATE TABLE room (
                      id SERIAL PRIMARY KEY,
                      room_number VARCHAR(20) NOT NULL,
                      base_price NUMERIC(10,2) NOT NULL CHECK (base_price >= 0),
                      bed_count INT NOT NULL CHECK (bed_count > 0),
                      status VARCHAR(20) DEFAULT 'AVAILABLE' NOT NULL CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'CLEANING', 'OUT_OF_SERVICE')),
                      room_type VARCHAR(20) NOT NULL CHECK (room_type IN ('STANDARD', 'SUITE')),
                      room_count INT,
                      hotel_id INT NOT NULL REFERENCES hotel(id) ON DELETE CASCADE
);

CREATE TABLE room_amenity (
                              room_id INT NOT NULL REFERENCES room(id) ON DELETE CASCADE,
                              amenity_id INT NOT NULL REFERENCES amenity(id) ON DELETE CASCADE,
                              PRIMARY KEY (room_id, amenity_id)
);

CREATE TABLE employee (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(150) NOT NULL,
                          phone VARCHAR(30),
                          salary NUMERIC(10,2) NOT NULL CHECK (salary >= 0),
                          employee_type VARCHAR(20) NOT NULL CHECK (employee_type IN ('MANAGER', 'CLEANER', 'GUARD', 'COOK')),
                          efficacity NUMERIC(5,2),
                          patrol_zone VARCHAR(100),
                          bonus_salary_per_hour NUMERIC(10,2),
                          specialty VARCHAR(100),
                          manager_id INT REFERENCES employee(id) ON DELETE SET NULL,
                          hotel_id INT NOT NULL REFERENCES hotel(id) ON DELETE CASCADE
);

CREATE TABLE day_guard (
                           id SERIAL PRIMARY KEY,
                           guard_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE,
                           date DATE NOT NULL,
                           start_time TIME NOT NULL,
                           end_time TIME NOT NULL,
                           shift_hour BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE reservation (
                             id SERIAL PRIMARY KEY,
                             guest_id INT NOT NULL REFERENCES guest(id) ON DELETE CASCADE,
                             room_id INT NOT NULL REFERENCES room(id) ON DELETE CASCADE,
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             status VARCHAR(20) DEFAULT 'PENDING' NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),
                             total_price NUMERIC(10,2) DEFAULT 0 NOT NULL,
                             CONSTRAINT reservation_check CHECK (end_date > start_date)
);

CREATE TABLE payment (
                         id SERIAL PRIMARY KEY,
                         reservation_id INT NOT NULL REFERENCES reservation(id) ON DELETE CASCADE,
                         amount NUMERIC(10,2) NOT NULL,
                         payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('CARD', 'CASH', 'BANK_TRANSFER', 'CHECK')),
                         status VARCHAR(20) DEFAULT 'PENDING' NOT NULL CHECK (status IN ('PENDING', 'VALIDATED', 'FAILED', 'REFUNDED')),
                         payment_date TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE invoice (
                         id SERIAL PRIMARY KEY,
                         reservation_id INT NOT NULL REFERENCES reservation(id) ON DELETE CASCADE,
                         issue_date TIMESTAMP DEFAULT NOW() NOT NULL,
                         total_amount NUMERIC(10,2) DEFAULT 0 NOT NULL
);

CREATE TABLE invoice_line (
                              id SERIAL PRIMARY KEY,
                              invoice_id INT NOT NULL REFERENCES invoice(id) ON DELETE CASCADE,
                              description VARCHAR(255) NOT NULL,
                              amount NUMERIC(10,2) NOT NULL
);

CREATE TABLE menu (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      hotel_id INT REFERENCES hotel(id) ON DELETE CASCADE
);

CREATE TABLE menu_dish (
                           menu_id INT NOT NULL REFERENCES menu(id) ON DELETE CASCADE,
                           dish_id INT NOT NULL REFERENCES dish(id) ON DELETE CASCADE,
                           PRIMARY KEY (menu_id, dish_id)
);

CREATE TABLE dish_ingredient (
                                 dish_id INT NOT NULL REFERENCES dish(id) ON DELETE CASCADE,
                                 ingredient_id INT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
                                 quantity_needed NUMERIC(10,2) NOT NULL,
                                 PRIMARY KEY (dish_id, ingredient_id)
);

CREATE TABLE restaurant_order (
                                  id SERIAL PRIMARY KEY,
                                  guest_id INT NOT NULL REFERENCES guest(id) ON DELETE CASCADE,
                                  room_id INT REFERENCES room(id) ON DELETE SET NULL,
                                  cook_id INT REFERENCES employee(id) ON DELETE SET NULL,
                                  status VARCHAR(20) DEFAULT 'IN_PREPARATION' NOT NULL CHECK (status IN ('IN_PREPARATION', 'READY', 'DELIVERED', 'CANCELLED')),
                                  order_date TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE order_line (
                            id SERIAL PRIMARY KEY,
                            order_id INT NOT NULL REFERENCES restaurant_order(id) ON DELETE CASCADE,
                            dish_id INT NOT NULL REFERENCES dish(id) ON DELETE CASCADE,
                            quantity INT NOT NULL CHECK (quantity > 0),
                            unit_price NUMERIC(10,2) NOT NULL
);


INSERT INTO hotel (id, name, address) VALUES (1, 'Extra Hotel Antananarivo', 'Analakely, Tananarive');
INSERT INTO amenity (id, name, additional_cost) VALUES (1, 'Wi-Fi Haut Débit', 0.00), (2, 'Mini-bar Premium', 25000.00);

INSERT INTO room (id, room_number, base_price, bed_count, status, room_type, room_count, hotel_id)
VALUES (101, 'Chambre 101', 120000.00, 1, 'AVAILABLE', 'STANDARD', 1, 1);
INSERT INTO room (id, room_number, base_price, bed_count, status, room_type, room_count, hotel_id)
VALUES (202, 'Suite Royale 202', 350000.00, 2, 'AVAILABLE', 'SUITE', 2, 1);

INSERT INTO room_amenity (room_id, amenity_id) VALUES (101, 1), (202, 1), (202, 2);

INSERT INTO employee (id, name, phone, salary, employee_type, hotel_id)
VALUES (1, 'Raza', '+261340011122', 1500000.00, 'MANAGER', 1);
INSERT INTO employee (id, name, phone, salary, employee_type, manager_id, hotel_id)
VALUES (2, 'Sitraka', '+261321122233', 600000.00, 'CLEANER', 1, 1);

INSERT INTO ingredient (id, name, stock_quantity, unit, alert_threshold)
VALUES (1, 'Riz Malgache', 50.00, 'kg', 10.00), (2, 'Viande de Zébu', 30.00, 'kg', 5.00);

INSERT INTO dish (id, name, price, category, preparation_time_minutes)
VALUES (1, 'Romazava Traditionnel', 35000.00, 'MAIN_COURSE', 25);

INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity_needed)
VALUES (1, 1, 0.20), (1, 2, 0.25);

INSERT INTO guest (id, name, phone, email)
VALUES (10, 'Marianah Labelle', '+261334455566', 'marianah.labelle@example.com');

INSERT INTO employee (id, name, phone, salary, employee_type, manager_id, hotel_id, specialty)
VALUES (5, 'Chef Faly', '+261345566677', 1200000.00, 'COOK', 1, 1, 'Cuisine Traditionnelle et Pizzeria');

INSERT INTO dish (id, name, price, category, preparation_time_minutes)
VALUES (2, 'Jus de Tamarin Fresh', 8000.00, 'BEVERAGE', 5),
       (3, 'Koba au Chocolat', 12000.00, 'DESSERT', 15);

INSERT INTO menu (id, name, hotel_id) VALUES (1, 'Carte Saveurs de Madagascar', 1);
INSERT INTO menu_dish (menu_id, dish_id) VALUES (1, 1), (1, 2), (1, 3);

INSERT INTO reservation (id, guest_id, room_id, start_date, end_date, status, total_price)
VALUES (100, 10, 202, '2026-06-25', '2026-06-28', 'CONFIRMED', 1050000.00);

INSERT INTO payment (id, reservation_id, amount, payment_method, status, payment_date)
VALUES (50, 100, 1050000.00, 'CARD', 'VALIDATED', '2026-06-23 10:15:00');

INSERT INTO invoice (id, reservation_id, issue_date, total_amount)
VALUES (200, 100, '2026-06-23 10:16:00', 1050000.00);

INSERT INTO invoice_line (id, invoice_id, description, amount)
VALUES (1, 200, 'Séjour Suite Royale 202 (3 nuits)', 1050000.00);

INSERT INTO restaurant_order (id, guest_id, room_id, cook_id, status, order_date)
VALUES (500, 10, 202, 5, 'READY', '2026-06-23 19:30:00');

INSERT INTO order_line (id, order_id, dish_id, quantity, unit_price)
VALUES (10, 500, 1, 1, 35000.00),
       (11, 500, 2, 1, 8000.00);

SELECT setval('hotel_id_seq', (SELECT MAX(id) FROM hotel));
SELECT setval('amenity_id_seq', (SELECT MAX(id) FROM amenity));
SELECT setval('guest_id_seq', (SELECT MAX(id) FROM guest));
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('dish_id_seq', (SELECT MAX(id) FROM dish));
SELECT setval('room_id_seq', (SELECT MAX(id) FROM room));
SELECT setval('employee_id_seq', (SELECT MAX(id) FROM employee));
SELECT setval('reservation_id_seq', (SELECT MAX(id) FROM reservation));
SELECT setval('payment_id_seq', (SELECT MAX(id) FROM payment));
SELECT setval('invoice_id_seq', (SELECT MAX(id) FROM invoice));
SELECT setval('invoice_line_id_seq', (SELECT MAX(id) FROM invoice_line));
SELECT setval('menu_id_seq', (SELECT MAX(id) FROM menu));
SELECT setval('restaurant_order_id_seq', (SELECT MAX(id) FROM restaurant_order));
SELECT setval('order_line_id_seq', (SELECT MAX(id) FROM order_line));