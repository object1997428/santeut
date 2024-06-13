package com.santeut.hiking.repository;

import com.santeut.hiking.vo.Room;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class RoomRepository {
    private final Map<Integer, Room> rooms;

    public RoomRepository() {
        rooms = new HashMap<>();
    }

    public Room room(int id) {
        return rooms.get(id);
    }

    public Room createRoom(int partyId, int userId) {
        Room room = Room.create(partyId, userId);
        rooms.put(partyId, room);
        return room;
    }
}
