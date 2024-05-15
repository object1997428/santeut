package com.santeut.party.common.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.party.dto.chatting.ChatMessageRequest;
import com.santeut.party.dto.chatting.ChatMessageResponse;
import com.santeut.party.dto.chatting.SocketDto;
import com.santeut.party.feign.UserInfoAccessUtil;
import com.santeut.party.feign.dto.response.UserInfoFeignResponseDto;
import com.santeut.party.repository.RoomRepository;
import com.santeut.party.service.ChatService;
import com.santeut.party.vo.Room;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

  private final RoomRepository roomRepository;
  private final UserInfoAccessUtil userInfoAccessUtil;
  private final ChatService chatService;
  private ObjectMapper om = new ObjectMapper();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    int userId = Integer.parseInt(session.getHandshakeHeaders().get("userId").get(0));
    Integer roomId = getRoomId(session);
    log.info("roomId={}",roomId);

    //Auth서버한테 유저 정보 요청
    String userNickname="";
    String userProfile="";
    log.info("[Party Server][Auth request url: /api/auth/user/{userId}");
    UserInfoFeignResponseDto responseEntity = userInfoAccessUtil.getUserInfo(userId);
    log.info("[Party Server][Auth response ={}",responseEntity);
    if (responseEntity != null) {
      userNickname = responseEntity.getUserNickname();
      userProfile = responseEntity.getUserProfile();
      log.info("[Party Server] Auth 한테 유저 정보 응답 받음 userInfo={}", userNickname);
    }

    SocketDto socketDto = SocketDto.builder()
        .userId(userId)
        .userNickname(userNickname)
        .userProfile(userProfile)
        .session(session)
        .build();
    //이미 userId가 존재하면 제거하고 새로 넣기
    roomRepository.room(roomId).addSession(socketDto);
    log.info("새 클라이언트와 연결되었습니다. partyId={}, socketDto={}",roomId,socketDto);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session,
      TextMessage message) throws IOException {
    String userId = session.getHandshakeHeaders().get("userId").get(0);
    Integer roomId = getRoomId(session);
    Room room = roomRepository.room(roomId);
    String payload = message.getPayload();
    log.info("Received message: {}", payload);

    try {
      JsonNode jsonNode = om.readTree(payload);
      String messageType = jsonNode.get("type").asText();
      if (messageType.equals("message")) {
        sendMessage(jsonNode, userId, room);
      } else {
        log.warn("Unknown message type received: {}", messageType);
      }
    } catch (Exception e) {
      log.error("Error processing message: ", e);
    }
  }

  private void sendMessage(JsonNode jsonNode, String userId, Room room) throws IOException {
    SocketDto fromDto = room.getSessionByUserId(Integer.parseInt(userId));
    log.info("partyId={}", room.getId());

    ChatMessageRequest request = om.treeToValue(jsonNode, ChatMessageRequest.class);
    LocalDateTime now = LocalDateTime.now();
    ChatMessageResponse response = ChatMessageResponse.of(request, now, Integer.parseInt(userId),
        fromDto.getUserNickname(), fromDto.getUserProfile());
    TextMessage textMessage = new TextMessage(om.writeValueAsString(response));
    // 메시지 저장
    chatService.saveMessage(now, room.getId(), response);

    room.getSessions().forEach((integer, socketDto) -> {
      WebSocketSession connectedSession=socketDto.getSession();
      log.info("message={}",textMessage);
      try {
        connectedSession.sendMessage(textMessage);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session,
      CloseStatus status) throws JsonProcessingException {
    int userId = Integer.parseInt(session.getHandshakeHeaders().get("userId").get(0));
    Integer roomId = getRoomId(session);

    roomRepository.room(roomId).removeSessionByUserId(userId);
    log.info("특정 클라이언트와의 연결이 해제되었습니다. partyId={}, userId={}",roomId,userId);
  }

  //세션 url에서 roomId가져옴
  private Integer getRoomId(WebSocketSession session) {
    return Integer.parseInt(
        session.getAttributes()
            .get("roomId")
            .toString()
    );
  }

}
