package com.santeut.hiking.vo;

import com.santeut.hiking.dto.websocket.SocketDto;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int id;

    private Map<Integer, SocketDto> sessions;  // Key: userId

    @Builder
    public static Room create(int partyId) {
        Room room = new Room();
        room.id =partyId;
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
