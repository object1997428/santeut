package com.santeut.hiking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.hiking.dto.request.LocationData;
import com.santeut.hiking.dto.websocket.LocationRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationSaveService {
    private final RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void locationSave(LocationRequestMessage locationDto
                             ) throws JsonProcessingException {
        LocationData locationData = LocationData.builder()
                .lat(locationDto.getLat())
                .lng(locationDto.getLng())
                .build();
        String locationJson = objectMapper.writeValueAsString(locationData);
        String redisKey = "partyId:"+locationDto.getPartyId()+":user:" + locationDto.getUserId()+":location";

        ListOperations<String, String> listOps = redisTemplate.opsForList();
        listOps.rightPush(redisKey, locationJson);
    }
}
