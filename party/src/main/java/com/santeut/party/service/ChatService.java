package com.santeut.party.service;

import com.santeut.party.dto.chatting.ChatMessageResponse;
import com.santeut.party.dto.response.ChatMessageListResponse;
import com.santeut.party.dto.response.ChatRoomListResponse;
import java.time.LocalDateTime;

public interface ChatService {

  public void saveMessage(LocalDateTime time, int partyId, ChatMessageResponse message);

  ChatRoomListResponse findMyChatRoom(int userId);

  ChatMessageListResponse getAllChatMessage(int userId, int partyId);

}
