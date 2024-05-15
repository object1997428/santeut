package com.santeut.party.vo;

import com.santeut.party.dto.chatting.SocketDto;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  private int id;
  private Map<Integer, SocketDto> sessions;

  @Builder
  public static Room create(int partyId) {
    Room room = new Room();
    room.id = partyId;
    room.sessions = new HashMap<>();
    return room;
  }

  public void addSession(SocketDto socketDto) {
    sessions.remove(socketDto.getUserId());
    sessions.put(socketDto.getUserId(), socketDto);
  }

  public void removeSessionByUserId(int userId) {
    SocketDto dto = sessions.remove(userId);
  }

  public SocketDto getSessionByUserId(int userId) {
    return sessions.get(userId);
  }

}
