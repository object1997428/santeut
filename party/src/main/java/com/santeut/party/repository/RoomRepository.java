package com.santeut.party.repository;

import com.santeut.party.vo.Room;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

  private final Map<Integer, Room> rooms = new HashMap<>();

  public Room room(int id) {
    return rooms.computeIfAbsent(id, Room::create);
  }

}
