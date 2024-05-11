package com.santeut.party.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.party.dto.chatting.ChatMessage;
import com.santeut.party.dto.chatting.ChatMessageDto;
import com.santeut.party.repository.ChatMessageRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

  private final ChatMessageRepository chatMessageRepository;
  private final ObjectMapper mapper;

  @Override
  public <T> void sendMessage(WebSocketSession session, T message) {
    try {
      session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public void saveMessage(ChatMessageDto messageDto) {
    chatMessageRepository.save(ChatMessage.from(messageDto));
  }

  @Override
  public String getLatestMessageByPartyId(int partyId) {
    Optional<ChatMessage> latestMessage =
        chatMessageRepository.findTopByPartyIdOOrderByCreated_atDesc(partyId);
    return (latestMessage.isEmpty()) ? "" : latestMessage.get().getContent();
  }

}
