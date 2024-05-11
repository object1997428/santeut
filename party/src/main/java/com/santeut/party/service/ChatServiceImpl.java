package com.santeut.party.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.party.dto.chatting.ChatMessage;
import com.santeut.party.dto.chatting.ChatMessageDto;
import com.santeut.party.dto.response.ChatMessageListResponse;
import com.santeut.party.dto.response.ChatMessageListResponse.ChatMessageInfoDto;
import com.santeut.party.dto.response.ChatRoomListResponse;
import com.santeut.party.dto.response.ChatRoomListResponse.ChatRoomInfo;
import com.santeut.party.entity.Party;
import com.santeut.party.feign.GuildAccessUtil;
import com.santeut.party.repository.ChatMessageRepository;
import com.santeut.party.repository.PartyUserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  private final PartyUserRepository partyUserRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final ObjectMapper mapper;
  private final GuildAccessUtil guildAccessUtil;

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
  public ChatRoomListResponse findMyChatRoom(int userId) {
    List<ChatRoomInfo> chatRoomInfoList = new ArrayList<>();
    List<Party> partyList = partyUserRepository.findMyPartyForChat(userId);

    for (Party p : partyList) {
      String guildName = p.getGuildId() == null ? ""
          : guildAccessUtil.getGuildInfo(p.getGuildId()).getGuildName(); // 동호회 이름
      String lastMessage = null;
      LocalDateTime lastSentDate = null;

      Optional<ChatMessage> chatMessage = chatMessageRepository.findTop1ByPartyIdOrderByCreatedAtDesc(
          p.getPartyId());
      if (chatMessage.isPresent()) {
        lastMessage = chatMessage.get().getContent();
        lastSentDate = chatMessage.get().getCreatedAt();
      }
      chatRoomInfoList.add(
          ChatRoomInfo.of(p.getPartyId(), p.getPartyName(), guildName, p.getParticipants(),
              lastMessage,
              lastSentDate));
    }
    return new ChatRoomListResponse(chatRoomInfoList);
  }

  @Override
  public ChatMessageListResponse getAllChatMessage(int userId, int partyId) {
    List<ChatMessageInfoDto> messageList = new ArrayList<>();
    List<ChatMessage> messages = chatMessageRepository.findAllBy(partyId);
    for (ChatMessage message : messages) {
      messageList.add(
          ChatMessageInfoDto.of(message.getCreatedAt(), "작성자이름", "프로필사진", message.getContent()));
    }

    return new ChatMessageListResponse(messageList);
  }
}
