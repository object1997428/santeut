package com.santeut.hiking.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SocketTextHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        log.info("[SocketTextHandler]: 새 클라이언트와 연결되었습니다.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws IOException {
        System.out.println(message.getPayload());
        log.info("[SocketTextHandler]: 클라이언트에게 메세지 전송");
        for (WebSocketSession connectedSession : sessions) {
            connectedSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {
        sessions.remove(session);
        log.info("[SocketTextHandler]: 특정 클라이언트와의 연결이 해제되었습니다. status={}",status);
    }
}
