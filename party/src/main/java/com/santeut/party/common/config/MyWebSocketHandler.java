package com.santeut.party.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.party.dto.chatting.ChatMessageDto;
import com.santeut.party.service.ChatService;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

  private final ObjectMapper objectMapper;
  private final ChatService chatService;
  private final Set<WebSocketSession> sessions = new HashSet<>();
  private final Map<Integer, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

  @Override
  protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    super.handleBinaryMessage(session, message);
  }

  // 소켓 연결 확인
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("{} 연결됨", session.getId());
    sessions.add(session);
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    super.handleMessage(session, message);
  }

  // 메시지 전송
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    log.info("payload {}", payload);

    // payload -> chatMessage
    ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
    int chatRoomId = chatMessage.getPartyId();
    // 채팅방 세션없으면 생성
    if(!chatRoomSessionMap.containsKey(chatRoomId)) {
      log.info("{}번 방 세션 생성", chatRoomId);
      chatRoomSessionMap.put(chatRoomId, new HashSet<>());
    }
    Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

    // message 타입 확인
    if(chatMessage.getType().equals(ChatMessageDto.MessageType.ENTER)) {
      log.info("{}님이 {}번 방에 입장", chatMessage.getUserNickname(), chatMessage.getPartyId());
      chatRoomSession.add(session);
    } else {
      sendMessageToChatRoom(chatMessage, chatRoomSession);
      chatService.saveMessage(chatMessage);
    }

  }

  // 소켓 종료 확인
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("{} 연결 끊김", session.getId());
    sessions.remove(session);
  }

  // 채팅 관련 메소드
  private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
    chatRoomSession.removeIf(session -> !sessions.contains(session));
  }

  private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
    chatRoomSession.parallelStream().forEach(s -> sendMessage(s, chatMessageDto));
  }

  private <T> void sendMessage(WebSocketSession session, T message) {
    try {
      session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    } catch(IOException e) {
      log.error(e.getMessage(), e);
    }
  }

}
