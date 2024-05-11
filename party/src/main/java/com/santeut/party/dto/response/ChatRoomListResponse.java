package com.santeut.party.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomListResponse {

  public List<ChatRoomInfo> chatRoom;

  @Data
  @Builder
  public static class ChatRoomInfo {
    public int partyId;
    public String partyName;
    public String guildName;
    public int peopleCnt;
    public String lastMessage;
    public String lastSentDate;

    public static ChatRoomInfo of(int partyId, String partyName, String guildName, int peopleCnt,
        String lastMessage, LocalDateTime lastSentDate) {
      return ChatRoomInfo.builder()
          .partyId(partyId)
          .partyName(partyName)
          .guildName(guildName)
          .peopleCnt(peopleCnt)
          .lastMessage(lastMessage)
          .lastSentDate(lastSentDate==null ? null : lastSentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
          .build();
    }
  }

}
