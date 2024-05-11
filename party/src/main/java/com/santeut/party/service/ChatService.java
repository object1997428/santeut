package com.santeut.party.service;

import com.santeut.party.dto.chatting.ChatMessageDto;
import com.santeut.party.dto.response.ChatRoomListResponse;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {

  // 메시지 전송
  public <T> void sendMessage(WebSocketSession session, T message);

  public void saveMessage(ChatMessageDto messageDto);

  ChatRoomListResponse findMyChatRoom(int userId);
}
