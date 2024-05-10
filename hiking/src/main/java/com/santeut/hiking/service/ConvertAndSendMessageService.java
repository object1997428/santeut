//package com.santeut.hiking.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.santeut.hiking.common.util.MessageIdGenerator;
//import com.santeut.hiking.dto.request.LocationData;
//import com.santeut.hiking.dto.response.MessageLocationResponseDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ConvertAndSendMessageService {
//    private final SimpMessagingTemplate template;
//    private final RedisTemplate<String, String> redisTemplate;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Transactional
//    public void convertAndSendMessage(String type,
//                                      Integer partyId,
//                                      Integer userId,
//                                      String userNickname,
//                                      Double lat, Double rat
//    ) throws JsonProcessingException {
//        template.convertAndSend(
//                "/sub/websocket/room/" + partyId,
//                new MessageLocationResponseDto(
//                        MessageIdGenerator.generateId(),
//                        type,
//                        userId,
//                        userNickname,
//                        lat,
//                        rat
//                )
//        );
//        LocationData locationData = LocationData.builder()
//                .lat(lat)
//                .lng(rat)
//                .build();
//        String locationJson = objectMapper.writeValueAsString(locationData);
//        String redisKey = "partyId:"+partyId+":user:" + userId+":location";
//
//        ListOperations<String, String> listOps = redisTemplate.opsForList();
//        listOps.rightPush(redisKey, locationJson);
//    }
//}
