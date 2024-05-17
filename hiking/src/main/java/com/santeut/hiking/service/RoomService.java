package com.santeut.hiking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.hiking.common.exception.FeginFailerException;
import com.santeut.hiking.common.response.BasicResponse;
import com.santeut.hiking.dto.response.GetPartyUserIdResponse;
import com.santeut.hiking.dto.websocket.SocketDto;
import com.santeut.hiking.feign.HikingPartyClient;
import com.santeut.hiking.repository.RoomRepository;
import com.santeut.hiking.vo.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final HikingPartyClient hikingPartyClient;
    private ObjectMapper om = new ObjectMapper();

    public Room getOrCreateRoom(int partyId) {
        Room room = roomRepository.room(partyId);
        if (room == null) {
            int partyUserId = getPartyUserId(partyId); // 소모임장 userId 요청
            if (partyUserId == -1) throw new FeginFailerException("Party 서버에게 소모임장 userId 정보 요청 실패");

            room = roomRepository.createRoom(partyId, partyUserId);
        }
        return room;
    }
    private int getPartyUserId(int partyId) {
        log.info("[Hiking Server][Party request url: /api/party/hiking/{partyId}/userId");
        ResponseEntity<?> partyResp = hikingPartyClient.getPartyUserId(partyId);
        log.info("[Hiking Server][Party response ={}",partyResp);
        if (partyResp.getStatusCode().is2xxSuccessful()) {
            BasicResponse basicResponse = om.convertValue(partyResp.getBody(), BasicResponse.class);
            if (basicResponse != null && basicResponse.getData() != null) {
                GetPartyUserIdResponse feignResp = om.convertValue(basicResponse.getData(), GetPartyUserIdResponse.class);
                int partyUserId = feignResp.getUserId();
                log.info("[Party Server] Party 한테 소모임장 userId 요청 받은 partyUserId={}", partyUserId);
                return partyUserId;
            }
        } else {
            log.error("[Party Server] Party 한테 소모임장 userId 요청 실패 partyResp={}",partyResp);
        }
        return -1;
    }

    public void addSession(int roomId, SocketDto socketDto){
        roomRepository.room(roomId).addSession(socketDto);
    }

    public void removeSession(int roomId,int userId) {
        roomRepository.room(roomId).removeSessionByUserId(userId);
    }
}
