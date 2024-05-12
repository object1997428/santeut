package com.santeut.party.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public class ChatMessageListResponse {

  public List<ChatMessageInfoDto> chatMessage;

  @Data
  @Builder
  public static class ChatMessageInfoDto {
    public String createdAt;
    public String senderNickname;
    public String senderProfile;
    public String content;

    public static ChatMessageInfoDto of(LocalDateTime createdAt, String senderNickname,
        String senderProfile, String content) {
      return ChatMessageInfoDto.builder()
          .createdAt(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
          .senderNickname(senderNickname)
          .senderProfile(senderProfile)
          .content(content)
          .build();
    }
  }

}
