//package com.santeut.hiking.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.santeut.hiking.dto.request.MessageLocationDto;
//import com.santeut.hiking.dto.request.MessageRequestDto;
//import com.santeut.hiking.service.ConvertAndSendMessageService;
//import com.santeut.hiking.service.EnterRoomService;
//import com.santeut.hiking.service.HikingDataScheduler;
//import com.santeut.hiking.service.QuitRoomService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Random;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class MessageController {
//    private final EnterRoomService enterRoomService;
//    private final QuitRoomService quitRoomService;
//    private final ConvertAndSendMessageService convertAndSendMessageService;
//    private final HikingDataScheduler hikingDataScheduler;
//
//    @GetMapping("/")
//    public String hello() {
//        return "hello";
//    }
//
//    @MessageMapping("/enter")
//    public void enter(MessageRequestDto messageRequestDto) {
//        enterRoomService.enterRoom(
//                messageRequestDto.getType(),
//                messageRequestDto.getPartyId(),
//                messageRequestDto.getUserId()
//        );
//        hikingDataScheduler.startTracking(messageRequestDto.getPartyId().toString(), messageRequestDto.getUserId().toString());
//    }
//
//    @MessageMapping("/quit")
//    public void quit(MessageRequestDto messageRequestDto) throws JsonProcessingException {
//        log.info("messageRequestDto2={}", messageRequestDto);
//        quitRoomService.quitRoom(
//                messageRequestDto.getType(),
//                messageRequestDto.getPartyId(),
//                messageRequestDto.getUserId()
//        );
//        hikingDataScheduler.stopTascking(messageRequestDto.getPartyId().toString(), messageRequestDto.getUserId().toString());
//    }
//
//    @MessageMapping("/message")
//    public void message(MessageLocationDto messageRequestDto) throws JsonProcessingException {
//        log.info("messageRequestDto3={}", messageRequestDto);
//        convertAndSendMessageService.convertAndSendMessage(
//                messageRequestDto.getType(),
//                messageRequestDto.getPartyId(),
//                messageRequestDto.getUserId(),
//                messageRequestDto.getUserNickname(),
//                messageRequestDto.getLat(),
//                messageRequestDto.getLng()
//        );
//
//    }
//
//    @MessageExceptionHandler
//    public String exception() {
//        return "Error has occurred.";
//    }
//}
