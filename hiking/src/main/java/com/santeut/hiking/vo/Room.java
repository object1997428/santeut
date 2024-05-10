package com.santeut.hiking.vo;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int id;

    private Set<WebSocketSession> sessions;

    @Builder
    public static Room create(int partyId) {
        Room room = new Room();
        room.id =partyId;
        room.sessions = new HashSet<>();
        return room;
    }

//    @Getter
//    public Set<WebSocketSession> getSessions() {
//        return sessions;
//    }
}
