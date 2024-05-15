package com.santeut.party.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.party.dto.chatting.ChatMessage;
import com.santeut.party.dto.chatting.ChatMessageDto;
import com.santeut.party.dto.chatting.ChatMessageResponse;
import com.santeut.party.dto.response.ChatMessageListResponse;
import com.santeut.party.dto.response.ChatMessageListResponse.ChatMessageInfoDto;
import com.santeut.party.dto.response.ChatRoomListResponse;
import com.santeut.party.dto.response.ChatRoomListResponse.ChatRoomInfo;
import com.santeut.party.entity.Party;
import com.santeut.party.feign.GuildAccessUtil;
import com.santeut.party.feign.UserInfoAccessUtil;
import com.santeut.party.feign.dto.response.GetPartyMemberInfoResponse;
import com.santeut.party.feign.dto.response.GetPartyMemberInfoResponse.PartyMemberInfo;
import com.santeut.party.repository.ChatMessageRepository;
import com.santeut.party.repository.PartyUserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final UserInfoAccessUtil userInfoAccessUtil;

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
  public void saveMessage(LocalDateTime time, int partyId, ChatMessageResponse message) {
    chatMessageRepository.save(ChatMessage.builder()
        .partyId(partyId)
        .userId(message.getUserId())
        .content(message.getContent())
        .createdAt(time)
        .build());
  }

  @Override
  public ChatRoomListResponse findMyChatRoom(int userId) {
    List<ChatRoomInfo> chatRoomInfoList = new ArrayList<>();
    List<Party> partyList = partyUserRepository.findMyPartyForChat(userId);

    for (Party p : partyList) {
      String guildName = p.getGuildId() == null ? ""
          : guildAccessUtil.getGuildInfo(p.getGuildId(), userId).getGuildName(); // 동호회 이름
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

    Map<Integer, PartyMemberInfo> partyMemberInfoMap = findMemberInParty(userId, partyId);

    List<ChatMessageInfoDto> messageList = new ArrayList<>();
    for (ChatMessage message : chatMessageRepository.findAllBy(partyId)) {
      PartyMemberInfo sender = partyMemberInfoMap.getOrDefault(message.getUserId(), null);
      messageList.add(ChatMessageInfoDto.of(
          message.getCreatedAt(),
          (sender == null) ? "나간 사용자" : sender.getUserNickname(),
          (sender == null) ? null : sender.getUserProfile(),
          message.getContent()
      ));
    }

    return new ChatMessageListResponse(messageList);
  }

  private Map<Integer, PartyMemberInfo> findMemberInParty(int userId, int partyId) {
    GetPartyMemberInfoResponse memberInfoList = userInfoAccessUtil.getPartyMemberInfo(partyId,
        userId, partyUserRepository.findAllMemberByPartyId(partyId));

    Map<Integer, PartyMemberInfo> partyMemberInfoMap = new HashMap<>();
    for (PartyMemberInfo memberInfo : memberInfoList.getPartyMembers()) {
      log.info("{}번: {}",memberInfo.getUserId(), memberInfo.getUserNickname());
      partyMemberInfoMap.put(memberInfo.getUserId(), memberInfo);
    }
    return partyMemberInfoMap;
  }
}
