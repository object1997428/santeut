package com.santeut.party.dto.chatting;

import com.santeut.party.common.util.CollectionIdGenerator;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

  @Id
  private String id;
  @Indexed
  private int partyId;    // 소모임 id
  private int userId;     // 전송자 유저 id
  private String content;  // 메시지 내용
  private LocalDateTime createdAt; // 전송 시각


  public static ChatMessage from(ChatMessageDto chatMessageDto) {
    return ChatMessage.builder()
        .partyId(chatMessageDto.getPartyId())
        .userId(chatMessageDto.getUserId())
        .content(chatMessageDto.getContent())
        .createdAt(LocalDateTime.now())
        .build();
  }

  public static ChatMessage of(LocalDateTime time, int partyId, int userId, String content) {
    return ChatMessage.builder()
        .partyId(partyId)
        .userId(userId)
        .content(content)
        .createdAt(time)
        .build();
  }

}
