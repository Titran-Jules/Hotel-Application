# Lemuri Hotel Mada — Hotel Management System

A robust, multi-role console application built in Java to streamline hotel operations, handle real-time room reservations, manage room service workflows, and aggregate staff payroll.

This project incorporates strict database constraints and business logic.

---

##  Key Features

The application operates on a role-based authentication flow, branching into four specialized dashboards:

###  Guest Espace (Client)
* **Live Catalog:** Browse currently vacant rooms (`StandardRoom` or `SuiteRoom`) mapped directly from the database.
* **Temporal Reservations:** Book rooms by specifying checking dates. The system prevents double-booking using precise date-range overlap checks.
* **Room Service:** Order traditional dishes and beverages straight to your room number.

###  Management Espace (Manager)
* **Reservation Tracking:** List all `PENDING` bookings awaiting action.
* **Billing & Invoicing:** Approve reservations, validate payments, and generate structured invoice logs.
* **Payroll Aggregation:** Dynamically calculate the hotel's gross salary expenses using aggregated SQL queries and data mapping.

###  Kitchen Hub (Cook)
* **Order Monitoring:** Track room service tickets marked as `IN_PREPARATION`.
* **Smart Cooking Logic:** Assign chefs to specific tickets, cooking meals while automatically deducting used ingredients from the stock.
* **Inventory Control:** Monitor the pantry (`stock_quantity`) and flag ingredient levels below critical alert thresholds.

### Floor Services (Cleaner)
* **Housekeeping Queue:** View a live queue of rooms requiring sanitation (marked as `CLEANING`).
* **Protocol Validation:** Execute cleaning protocols to safely reset room statuses back to `AVAILABLE`.

---

##  Tech Stack & Architecture

* **Language:** Java SE (Object-Oriented Architecture with dedicated Services, Models, and DAOs)
* **Database:** PostgreSQL 15+
* **Persistence Layer:** JDBC (Java Database Connectivity) utilizing Connection Pooling and transaction safety (`commit`/`rollback`).
* **Design Patterns:** Data Access Object (DAO) pattern with `GenericDAO<T, ID>` interfaces for clear separation of concerns.

---

## Getting Started

### 1. Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher.
* **Database Engine:** PostgreSQL running locally or remotely.

### 2. Database Initialization
Navigate to `src/main/resources/` and run the provided SQL script to build the schema structure along with ready-to-test seed data:

```bash
psql -U your_username -d your_database_name -f src/main/resources/schema.sql
