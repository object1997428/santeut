//package com.santeut.hiking.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.santeut.hiking.common.code.WebSocketCode;
//import com.santeut.hiking.common.util.MessageIdGenerator;
//import com.santeut.hiking.dto.request.MessageLocationDto;
//import com.santeut.hiking.dto.response.MessageLocationResponseDto;
//import com.santeut.hiking.dto.websocket.CheckDto;
//import com.santeut.hiking.dto.websocket.SocketDto;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class WebSocketService {
//    private final ObjectMapper om;
//
//    private Map<Integer, SocketDto> users;
//
//    //왜 얘는
//    @PostConstruct
//    void init() {
//        users = new HashMap<>();
//    }
//
//    public void check(SocketDto socketDto) throws JsonProcessingException {
//
//        try {
//            if (users.containsKey(socketDto.getUserId())) {
//                log.info("[WebSocketService][check()] 해당 userId에 대한 기존 세션이 존재함, 지울 예정 userId={}, 현재 연결 세션={}", socketDto.getUserId(), users.get(socketDto.getUserId()));
//                users.remove(socketDto.getUserId());
//            }
//            log.info("[WebSocketService][check()] 해당 userId에 대한 New 세션을 체크-- userId={}", socketDto.getUserId());
//            TextMessage message = new TextMessage(om.writeValueAsString(new CheckDto(WebSocketCode.CHECK)));
//            socketDto.getSession().sendMessage(message);
//        } catch (IOException e) {
//            log.error("[WebSocketService][check()]메세지 생성 실패");
//        }
//    }
//
//    public void connect(SocketDto socketDto){
//        log.info("새로운 세션을 등록합니다. {}", socketDto.getUserId());
//        users.put(socketDto.getUserId(), socketDto);
//    }
//
//    public void disconnect(SocketDto socketDto){
//        if(users.containsKey(socketDto.getUserId())){
//            log.info("세션을 삭제합니다. memberId : {}", socketDto.getUserId());
//            users.remove(socketDto.getUserId());
//        }else{
//            log.info("매칭되는 세션이 없습니다.");
//        }
//    }
//
//    public <T> void sendMessage(WebSocketSession session, T message) {
//        try {
//            System.out.println(message);
//            if (session.isOpen()) {
//                session.sendMessage(new TextMessage(om.writeValueAsString(message)));
//            }
//        } catch (IOException e) {
//            log.error("세션 만료");
//        }
//    }
//
//    public void sendLocation(MessageLocationDto location) {
//        try {
//            if(!users.containsKey(location.getUserId())){
//                log.info("{}와 연결된 소켓이 없습니다.", location.getUserId());
//            }else{
//                TextMessage message = new TextMessage(om.writeValueAsString(new MessageLocationResponseDto(
//                        MessageIdGenerator.generateId(),
//                        location.getType(),
//                        location.getUserId(),
//                        location.getUserNickname(),
//                        location.getLat(),
//                        location.getLng()
//                )));
//                SocketDto socketDto = users.get(location.getUserId());
//                log.info("연결된 세션 : {} memberId : {}",socketDto, location.getUserId());
//                if(socketDto.getSession().isOpen()){
//                    try {
//                        log.info("{}에 상태 메세지를 보냅니다.", location.getUserId());
//                        socketDto.getSession().sendMessage(message);
//                    } catch (IOException e) {
//                        log.info("응 못보내");
//                    }
//                }
//                else{
//                    users.remove(socketDto.getUserId());
//                    log.info("해당 세션은 유효하지 않아서 삭제함 userId={}",location.getUserId());
//                }
//            }
//        }catch (IOException e){
//            log.info("메세지 생성 실패");
//        }
//
//    }
//}
