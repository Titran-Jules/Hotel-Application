package com.hotelgestion.service;

import com.hotelgestion.dao.HotelDAO;
import com.hotelgestion.dao.RoomDAO;
import com.hotelgestion.model.Amenity;
import com.hotelgestion.model.Room;
import com.hotelgestion.model.RoomStatus;

import java.util.List;
import java.util.stream.Collectors;

public class RoomService {
    private final HotelDAO hotelDAO;
    private final RoomDAO roomDAO;

    public RoomService(HotelDAO hotelDAO, RoomDAO roomDAO) {
        this.hotelDAO = hotelDAO;
        this.roomDAO = roomDAO;
    }

    public List<Room> listAvailable(int hotelId)  {
        var hotel = hotelDAO.findWithRoomsAndEmployees(hotelId)
                .orElseThrow(() -> new RuntimeException("Hôtel introiuvable " + hotelId));

        return hotel.getRooms().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    public void changeStatus(int roomId, RoomStatus status) {
        var room = roomDAO.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chambre introuvable "+ roomId));

        room.changesStatus(status);
        roomDAO.update(room);
    }

    public void addAmenity(int roomId, Amenity a) {
        roomDAO.addAmenity(roomId, a.getId());
    }
}
