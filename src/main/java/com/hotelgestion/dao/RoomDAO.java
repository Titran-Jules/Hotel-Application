package com.hotelgestion.dao;

import com.hotelgestion.model.*;
import com.hotelgestion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO implements GenericDAO<Room, Integer>{
    @Override
    public Room create(Room room) {
        String sql = """
                INSERT INTO room (room_number, base_price, bed_count, status, room_type, room_count, hotel_id)
                VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setDouble(2, room.getBasePrice());
            stmt.setInt(3, room.getBedCount());
            stmt.setString(4, room.getStatus().name());

            if (room instanceof SuiteRoom suite) {
                stmt.setString(5, "SUITE");
                stmt.setInt(6, suite.getRoomCount());
            } else {
                stmt.setString(5, "STANDARD");
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setInt(7, room.getHotelId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    room.setId(rs.getInt("id"));
                }
            }
            return room;
        } catch (SQLException e) {
            throw new RuntimeException("Error during room's creation", e);
        }
    }

    @Override
    public Optional<Room> findById(Integer id) {
        String sql = "SELECT * FROM room WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during room's looking for"+ id, e);
        }
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT * FROM room";
        List<Room> rooms = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during getting all rooms", e);
        }
        return rooms;
    }

    public List<Room> findByStatus(RoomStatus status) {
        String sql = "SELECT * FROM room WHERE status = ?";
        List<Room> rooms = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during filtering by status", e);
        }
        return rooms;
    }

    public List<Room> findByHotelId(int hotelId) {
        String sql = "SELECT * FROM room WHERE hotel_id = ?";
        List<Room> rooms = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during filtering by hotel", e);
        }
        return rooms;
    }

    @Override
    public boolean update(Room room) {
        String sql = """
                UPDATE room SET room_number = ?, base_price = ?, bed_count = ?,
                                status = ?, room_count = ? WHERE id = ?
                """;
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setDouble(2, room.getBasePrice());
            stmt.setInt(3, room.getBedCount());
            stmt.setString(4, room.getStatus().name());
            if (room instanceof SuiteRoom suite) {
                stmt.setInt(5, suite.getRoomCount());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, room.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error during updating room"+ room.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM room WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error during deleting room "+ id, e);
        }
    }

    public void addAmenity(int roomId, int amenityId) {
        String sql = "INSERT INTO room_amenity (room_id, amenity_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.setInt(2, amenityId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error during adding amenity", e);
        }
    }

    private List<Amenity> findAmenitiesForRoom(int roomId) {
        String sql = """
                SELECT a.id, a.name, a.additional_cost FROM amenity a
                JOIN room_amenity ra ON ra.amenity_id = a.id
                WHERE ra.room_id = ?
                """;
        List<Amenity> amenities = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    amenities.add(new Amenity(rs.getInt("id"), rs.getString("name"), rs.getDouble("additional_cost")));
                }
            }
        }  catch (SQLException e) {
            throw new RuntimeException("Error during amenities's recuperation", e);
        }
        return amenities;
    }


    private Room mapRow(ResultSet rs) throws SQLException {
        String roomType = rs.getString("room_type");

        Room room;
        if ("SUITE".equals(roomType)) {
            SuiteRoom suite = SuiteRoom.builder().build();
            suite.setRoomCount(rs.getInt("room_count"));
            room = suite;
        } else {
            room = StandardRoom.builder().build();
        }

        room.setId(rs.getInt("id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setBasePrice(rs.getDouble("base_price"));
        room.setBedCount(rs.getInt("bed_count"));
        room.setStatus(RoomStatus.valueOf(rs.getString("status")));
        room.setHotelId(rs.getInt("hotel_id"));
        room.setAmenities(findAmenitiesForRoom(room.getId()));

        return room;
    }
}
