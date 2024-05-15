package com.santeut.party.service;

import com.santeut.party.dto.chatting.ChatMessageDto;
import com.santeut.party.dto.chatting.ChatMessageResponse;
import com.santeut.party.dto.response.ChatMessageListResponse;
import com.santeut.party.dto.response.ChatRoomListResponse;
import java.time.LocalDateTime;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {

  // 메시지 전송
  public <T> void sendMessage(WebSocketSession session, T message);

  public void saveMessage(ChatMessageDto messageDto);

  public void saveMessage(LocalDateTime time, int partyId, ChatMessageResponse message);

  ChatRoomListResponse findMyChatRoom(int userId);

  ChatMessageListResponse getAllChatMessage(int userId, int partyId);

}
