package com.santeut.hiking.service;

import com.santeut.hiking.common.util.MessageIdGenerator;
import com.santeut.hiking.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnterRoomService {

    private final SimpMessagingTemplate template;

    public void enterRoom(String type,
                          Integer partyId,
                          Integer userId) {
        log.info("[Hiking서버 - enterRoom] type={}, partyId={}, userId={}",type, partyId, userId);
        template.convertAndSend(
                "/sub/websocket/room/" + partyId,
                new MessageResponseDto(
                        MessageIdGenerator.generateId(),
                        type,
                        "사용자 " + userId + " 님이 "
                                + "채팅방 " + partyId + "에 입장하셨습니다."
                )
        );
        log.info("-- [Hiking서버 - enterRoom] type={}, partyId={}, userId={}",type, partyId, userId);
    }
}
