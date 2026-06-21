package com.hotelgestion.service;

import com.hotelgestion.dao.RoomDAO;
import com.hotelgestion.model.Amenity;
import com.hotelgestion.model.Room;
import com.hotelgestion.model.RoomStatus;

import java.util.List;
import java.util.stream.Collectors;

public class RoomService {
    private final RoomDAO roomDAO;

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public List<Room> listAvailable(int hotelId)  {
        List<Room> rooms = roomDAO.findByHotelId(hotelId);
        return rooms.stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    public void changeStatus(int roomId, RoomStatus status) {
        Room room = roomDAO.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chambre introuvable "+ roomId));

        room.changesStatus(status);
        roomDAO.update(room);
    }

    public void addAmenity(int roomId, Amenity a) {
        roomDAO.addAmenity(roomId, a.getId());
    }
}
