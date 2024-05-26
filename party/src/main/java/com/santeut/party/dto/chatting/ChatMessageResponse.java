package com.santeut.party.dto.chatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {

  private String createdAt; // 전송시각
  private int userId;     // 전송자 유저 id
  private String userNickname; // 전송자 유저 닉네임
  private String userProfile; // 전송자 유저 프로필
  private String content;  // 메시지 내용

  public static ChatMessageResponse of(ChatMessageRequest request, LocalDateTime now, int userId, String userNickname,
      String userProfile) {
    return ChatMessageResponse.builder()
        .createdAt(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        .userId(userId)
        .userNickname(userNickname)
        .userProfile(userProfile)
        .content(request.getContent())
        .build();
  }

}
