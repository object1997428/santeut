//package com.santeut.hiking.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.santeut.hiking.dto.request.HikingTrackSaveReignRequestDto;
//import com.santeut.hiking.dto.request.LocationData;
//import com.santeut.hiking.dto.request.TrackData;
//import com.santeut.hiking.feign.HikingPartyClient;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ScheduledFuture;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class HikingDataScheduler {
//    private final RedisTemplate<String,String> redisTemplate;
//    private final HikingPartyClient hikingPartyClient;
//    private ObjectMapper objectMapper = new ObjectMapper();
//    private final TaskScheduler taskScheduler;
//    private Map<String, ScheduledFuture<?>> jobsMap=new ConcurrentHashMap<>();
//
//    public void startTracking(String partyId,String userId){
//        log.info("[Scheduler]: startTracking -- partyId={}",partyId);
//        //파티에 대한 set이 비어있으면 -> 스케줄러에 파티 등록
//        if(!redisTemplate.hasKey(partyId)){
//            ScheduledFuture<?> scheduledTask=taskScheduler.scheduleWithFixedDelay(()->{
//                try {
//                    processLocationData(partyId);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
////            },600000);
//            },60000);
//            jobsMap.put(partyId,scheduledTask);
//            log.info("[Scheduler]: 등록! -- partyId={}",partyId);
//        }
//
//        //Redis에 스케줄러에서 처리할 유저 등록(파티 시작)
//        String key = "party:" + partyId;
//        String RedisKey = "partyId:"+partyId+":user:" + userId+":location";
//        SetOperations<String, String> setOps = redisTemplate.opsForSet();
//        setOps.add(key, userId); //party:{} <- userId
//    }
//
//    public void stopTascking(String partyId,String userId) throws JsonProcessingException {
//        log.info("[Scheduler]: stopTascking -- partyId={}",partyId);
//        //Redis에 스케줄러에서 유저 해제(파티 끝)
//        String key = "party:" + partyId;
//        String RedisKey = "partyId:"+partyId+":user:" + userId+":location";
//        SetOperations<String, String> setOps = redisTemplate.opsForSet();
//        setOps.remove(key,userId);
//
//        //Redis가 비었으면 스케줄러에서 지우기
//        if(!redisTemplate.hasKey(partyId)) {
//            String jobKey = "partyId:" + partyId + ":user:" + userId + ":location";
//            ScheduledFuture<?> scheduledTask = jobsMap.get(partyId);
//            if (scheduledTask != null) {
//                scheduledTask.cancel(false);
//                jobsMap.remove(partyId);
//            }
//            log.info("[Scheduler]: 등록 해제! -- partyId={}",partyId);
//        }
//    }
//
//
//    private void processLocationData(String partyId) throws JsonProcessingException {
//        log.info("[Scheduler]: processLocationData -- partyId={}",partyId);
//        //partyId에 대한 set 목록 가져오기
//        String key = "party:" + partyId;
//        Set<String> membserSet= redisTemplate.opsForSet().members(key);
//
//        List<TrackData> trackDataList=new ArrayList<>();
//        for (String userId : membserSet) {
//            //유저의 좌표모음 리스트에 담기
//            TrackData trackData = getTrackData(partyId, userId);
//            if(trackData!=null) trackDataList.add(trackData);
//        }
//
//        HikingTrackSaveReignRequestDto hikingTrackSaveReignRequestDto=HikingTrackSaveReignRequestDto.builder()
//                .partyId(Integer.parseInt(partyId))
//                .trackDataList(trackDataList)
//                .build();
//
//        //Party서버한테 트랙 저장 요청
//        log.info("[Hiking Server][Party request url: /api/party/hiking/track");
//        ResponseEntity<?> responseEntity = hikingPartyClient.saveHikingTrack(hikingTrackSaveReignRequestDto);
//        log.info("[Hiking Server][Party response ={}",responseEntity);
//    }
//
//    private TrackData getTrackData(String partyId, String userId) throws JsonProcessingException {
//
//        String redisKey = "partyId:"+partyId+":user:" + userId+":location";
//        log.info("[Scheduler]: partyId={}, userId={}, redisKey={}",partyId,userId,redisKey);
//        ListOperations<String, String> listOps = redisTemplate.opsForList();
//
//        //Redis에서 경로 좌표 꺼내오기
//        List<String> locationJsonList = listOps.range(redisKey, 0, -1);
//        if (locationJsonList == null) {
//            return null;
//        }
//
//        //Party 서버한테 MySQL에 저장 요청
//        TrackData trackData =new TrackData();
//        trackData.setUserId(Integer.parseInt(userId));
//
//        List<LocationData> locationDataList=new ArrayList<>(locationJsonList.size());
//        for (String locationJson : locationJsonList) {
//            locationDataList.add(objectMapper.readValue(locationJson, LocationData.class));
//        }
//        trackData.setLocationDataList(locationDataList);
//
//        // Redis에서 데이터 삭제
//        redisTemplate.delete(redisKey);
//        return trackData;
//    }
//}
