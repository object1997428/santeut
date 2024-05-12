package com.santeut.party.dto.chatting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {

  public enum MessageType {
    JOIN, ENTER, OUT, TALK
  }

  private int partyId;    // 소모임 id
  private int userId;     // 전송자 유저 id
  private String userNickname; // 전송자 유저 닉네임
  private String userProfile; // 전송자 유저 프로필
  private String content;  // 메시지 내용
  private MessageType type; // 메시지 타입

}
