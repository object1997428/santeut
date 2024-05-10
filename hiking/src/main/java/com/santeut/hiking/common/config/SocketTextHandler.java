package com.santeut.hiking.common.config;

import com.santeut.hiking.repository.RoomRepository;
import com.santeut.hiking.vo.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class SocketTextHandler extends TextWebSocketHandler {
    //    private final Map<WebSocketSession, SocketDto> sessions = new HashMap<>();
//    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Autowired
    private RoomRepository roomRepository;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer roomId = getRoomId(session);

        roomRepository.room(roomId).getSessions().add(session);

        System.out.println("새 클라이언트와 연결되었습니다.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws IOException {
        Integer roomId = getRoomId(session);

        Room room = roomRepository.room(roomId);

        System.out.println(message.getPayload());

        for (WebSocketSession connectedSession : room.getSessions()) {
            connectedSession.sendMessage(message);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {
        Integer roomId = getRoomId(session);
        roomRepository.room(roomId).getSessions().remove(session);
        
        log.info("특정 클라이언트와의 연결이 해제되었습니다.");
    }

    private Integer getRoomId(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri())
                .toString();
        String[] parts = uri.split("/");
        String roomId = parts[parts.length - 1];
        return Integer.parseInt(roomId);
    }
}
