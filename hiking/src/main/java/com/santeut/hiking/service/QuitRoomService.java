//package com.santeut.hiking.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.santeut.hiking.common.util.MessageIdGenerator;
//import com.santeut.hiking.dto.response.MessageLocationResponseDto;
//import com.santeut.hiking.dto.response.MessageResponseDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class QuitRoomService {
//    private final SimpMessagingTemplate template;
//
//    public void quitRoom(String type,
//                         Integer partyId,
//                         Integer userId) throws JsonProcessingException {
//        log.info("[Hiking서버 - quitRoom] type={}, partyId={}, userId={}",type, partyId, userId);
////        template.convertAndSend(
////                "/sub/websocket/room/" + partyId,
////                new MessageResponseDto(
////                        MessageIdGenerator.generateId(),
////                        type,
////                        "사용자 " + userId + " 님이 "
////                                + "채팅방 " + partyId + "에서 나갔습니다."
////                )
////        );
//    }
//}
