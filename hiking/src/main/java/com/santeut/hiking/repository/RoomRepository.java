package com.santeut.hiking.repository;

import com.santeut.hiking.vo.Room;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class RoomRepository {
    private final Map<Integer, Room> rooms;

    public RoomRepository() {
        rooms = Stream.of(
                Room.create(1),
                Room.create(2),
                Room.create(3)
        ).collect(Collectors.toMap(
                Room::getId,
                room -> room
        ));
    }

    public Room room(int id) {
       return rooms.computeIfAbsent(id, Room::create);
    }
}
