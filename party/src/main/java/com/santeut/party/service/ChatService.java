package com.santeut.party.service;

import com.santeut.party.dto.chatting.ChatMessageDto;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {

  // 메시지 전송
  public <T> void sendMessage(WebSocketSession session, T message);

  public void saveMessage(ChatMessageDto messageDto);

  // 가장 최신 메시지 조회
  String getLatestMessageByPartyId(int partyId);

}
