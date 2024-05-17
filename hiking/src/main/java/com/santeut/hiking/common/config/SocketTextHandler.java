package com.santeut.hiking.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.hiking.dto.response.GetUserInfoResponse;
import com.santeut.hiking.dto.websocket.EnterandQuitRequestMessage;
import com.santeut.hiking.dto.websocket.ResponseMessage;
import com.santeut.hiking.dto.websocket.RequestMessage;
import com.santeut.hiking.dto.websocket.SocketDto;
import com.santeut.hiking.feign.FeignResponseDto;
import com.santeut.hiking.feign.HikingAuthClient;
import com.santeut.hiking.repository.RoomRepository;
import com.santeut.hiking.service.HikingDataScheduler;
import com.santeut.hiking.service.LocationSaveService;
import com.santeut.hiking.vo.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class SocketTextHandler extends TextWebSocketHandler {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HikingDataScheduler hikingDataScheduler;
    @Autowired
    private LocationSaveService locationSaveService;
    private ObjectMapper om = new ObjectMapper();
    @Autowired
    private HikingAuthClient hikingAuthClient;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        int userId = Integer.parseInt(session.getHandshakeHeaders().get("userId").get(0));
        Integer roomId = getRoomId(session);
        log.info("roomId={}",roomId);

        //Auth서버한테 유저 정보 요청
        String userNickname="",userProfile="";
        log.info("[Hiking Server][Auth request url: /api/auth/user/{userId}");
        Optional<FeignResponseDto<GetUserInfoResponse>> responseEntity = hikingAuthClient.userInfo(userId);
        log.info("[Hiking Server][Auth response ={}",responseEntity);
        if (responseEntity != null && responseEntity.get().getData() != null) {
            GetUserInfoResponse userInfo = om.convertValue(responseEntity.get().getData(), GetUserInfoResponse.class);
            userNickname = userInfo.getUserNickname();
            userProfile=userInfo.getUserProfile();
            log.info("[Party Server] Auth 한테 유저 정보 응답 받음 userInfo={}", userInfo);
        }

        SocketDto socketDto = SocketDto.builder()
                .userId(userId)
                .userNickname(userNickname)
                .userProfile(userProfile)
                .session(session)
                .build();
        //이미 userId가 존재하면 제거하고 새로 넣기
        roomRepository.room(roomId).addSession(socketDto);
        log.info("새 클라이언트와 연결되었습니다. partyId={}, socketDto={}",roomId,socketDto);
        hikingDataScheduler.startTracking(roomId.toString(), Integer.toString(userId));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws IOException {
        String userId = session.getHandshakeHeaders().get("userId").get(0);
        Integer roomId = getRoomId(session);
        Room room = roomRepository.room(roomId);
        String payload = message.getPayload();
        log.info("Received message: {}", payload);

        try {
            JsonNode jsonNode = om.readTree(payload);
            String messageType = jsonNode.get("type").asText();
            switch (messageType) {
                case "healthLisk":
                    sendHealthLiskMessage(jsonNode,userId,room);
                    break;
                case "offCourse":
                    sendOffCourseMessage(jsonNode,userId,room);
                    break;
                case "locationShare":
                    sendLocationShareMessage(jsonNode, userId, room);
                    break;
                default:
                    log.warn("Unknown message type received: {}", messageType);
            }
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }

    private void sendHealthLiskMessage(JsonNode jsonNode, String userId, Room room) throws IOException {
        SocketDto fromDto = room.getSessionByUserId(Integer.parseInt(userId));
        log.info("partyId={}",room.getId());

        RequestMessage messageDto = om.treeToValue(jsonNode, RequestMessage.class);
        ResponseMessage messageRespDto = ResponseMessage.fromRequestDto(messageDto,Integer.parseInt(userId), fromDto.getUserNickname(), fromDto.getUserProfile());
        //redis에 위치 저장
        locationSaveService.locationSave(messageRespDto, room.getId());
        String responsePayload = om.writeValueAsString(messageRespDto);
        TextMessage textMessage = new TextMessage(responsePayload);

        room.getSessions().forEach((integer, socketDto) -> {
            WebSocketSession connectedSession=socketDto.getSession();
            log.info("message={}",textMessage);
            try {
                connectedSession.sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void sendOffCourseMessage(JsonNode jsonNode, String userId, Room room) throws IOException {
        SocketDto fromDto = room.getSessionByUserId(Integer.parseInt(userId));
        log.info("partyId={}",room.getId());

        RequestMessage messageDto = om.treeToValue(jsonNode, RequestMessage.class);
        ResponseMessage messageRespDto = ResponseMessage.fromRequestDto(messageDto,Integer.parseInt(userId), fromDto.getUserNickname(), fromDto.getUserProfile());
        //redis에 위치 저장
        locationSaveService.locationSave(messageRespDto, room.getId());
        String responsePayload = om.writeValueAsString(messageRespDto);
        TextMessage textMessage = new TextMessage(responsePayload);

        room.getSessions().forEach((integer, socketDto) -> {
            WebSocketSession connectedSession=socketDto.getSession();
            log.info("message={}",textMessage);
            try {
                connectedSession.sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void sendLocationShareMessage(JsonNode jsonNode, String userId, Room room) throws IOException {
        SocketDto fromDto = room.getSessionByUserId(Integer.parseInt(userId));
        log.info("partyId={}",room.getId());

        RequestMessage locationDto = om.treeToValue(jsonNode, RequestMessage.class);
        ResponseMessage locationRspDto = ResponseMessage.fromRequestDto(locationDto,Integer.parseInt(userId), fromDto.getUserNickname(), fromDto.getUserProfile());
        //redis에 위치 저장
        locationSaveService.locationSave(locationRspDto, room.getId());
        String responsePayload = om.writeValueAsString(locationRspDto);
        TextMessage textMessage = new TextMessage(responsePayload);

        room.getSessions().forEach((integer, socketDto) -> {
            WebSocketSession connectedSession=socketDto.getSession();
            log.info("message={}",textMessage);
            try {
                connectedSession.sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }




    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws JsonProcessingException {
        //웹소켓 퇴장 처리
        int userId = Integer.parseInt(session.getHandshakeHeaders().get("userId").get(0));
        Integer roomId = getRoomId(session);
        roomRepository.room(roomId).removeSessionByUserId(userId);
        log.info("특정 클라이언트와의 연결이 해제되었습니다. partyId={}, userId={}",roomId,userId);
        hikingDataScheduler.stopTascking(roomId.toString(), Integer.toString(userId));

        //userId가 파티장이면

    }

    //세션 url에서 roomId가져옴
    private Integer getRoomId(WebSocketSession session) {
        return Integer.parseInt(
                session.getAttributes()
                        .get("roomId")
                        .toString()
        );
    }
}
