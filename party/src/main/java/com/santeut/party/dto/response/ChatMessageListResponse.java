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
    public int userId;
    public String createdAt;
    public String userNickname;
    public String userProfile;
    public String content;

    public static ChatMessageInfoDto of(int userId, LocalDateTime createdAt, String userNickname,
        String userProfile, String content) {
      return ChatMessageInfoDto.builder()
          .userId(userId)
          .createdAt(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
          .userNickname(userNickname)
          .userProfile(userProfile)
          .content(content)
          .build();
    }
  }

}
