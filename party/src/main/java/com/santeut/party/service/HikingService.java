package com.santeut.party.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.party.common.exception.DataMismatchException;
import com.santeut.party.common.exception.DataNotFoundException;
import com.santeut.party.common.exception.PartyExpiredException;
import com.santeut.party.common.exception.PartyNotStartedException;
import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.util.GeometryUtils;
import com.santeut.party.dto.request.*;
import com.santeut.party.dto.response.HikingStartResponse;
import com.santeut.party.feign.HikingCommonClient;
import com.santeut.party.feign.dto.request.*;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.PartyUser;
import com.santeut.party.feign.HikingAuthClient;
import com.santeut.party.feign.HikingMountainClient;
import com.santeut.party.feign.dto.request.HikingRecordRequest;
import com.santeut.party.repository.PartyRepository;
import com.santeut.party.repository.PartyUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HikingService {
    private final PartyRepository partyRepository;
    private final PartyUserRepository partyUserRepository;
    private final HikingMountainClient hikingMountainClient;
    private final HikingAuthClient hikingAuthClient;
    private final HikingCommonClient hikingCommonClient;
    private final ObjectMapper om;
    private final RedisTemplate<String, Object> redisTemplate;

    private GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public HikingStartResponse hikingStart(int userId, HikingEnterRequest hikingEnterRequest) {
        Party party = partyRepository.findByPartyId(hikingEnterRequest.getPartyId())
                .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다."));

        if (party.getStatus() == 'E' || party.getStatus() == 'I')
            throw new PartyExpiredException("해당 소모임은 이미 종료되었습니다.");
        if (party.getSchedule().isAfter(LocalDateTime.now()))
            throw new PartyNotStartedException("해당 소모임은 아직 시작할 수 없습니다.");
        HikingStartResponse resp = null;
        if (party.getStatus() == 'B') {
            if (userId != party.getUserId())
                throw new PartyNotStartedException("아직 소모임장이 소모임을 활성화 하지 않았습니다.");
            //소모임 활성화
            party.setPartyStatus('P');
            partyRepository.save(party);

            //소모임장 입장 처리
            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingEnterRequest.getPartyId(), userId)
                    .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));
            if (partyUser.getStatus() == 'P') {
                log.error("Party가 시작 전(B)인데 PartyUser가 진행 중(P)이다");
                throw new DataMismatchException("소모임이 시작하지 않았는데 회원이 이미 소모임을 진행 중");
            }
            partyUser.setStatus('P',hikingEnterRequest.getStartTime());
            partyUserRepository.save(partyUser);

            //Mountain 서버한테 부탁해서 등산로 좌표 가져오기
            resp = getHikingTrack(party);

            //소모임 회원들한테 알림보내기 요청
            alertHikingStart(hikingEnterRequest, party);
        }
        else if (party.getStatus() == 'P') {
            //입장 처리
            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingEnterRequest.getPartyId(), userId)
                    .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));
            if (partyUser.getStatus() == 'P') throw new DataMismatchException("해당 유저는 이미 소모임을 진행 중입니다.");
            partyUser.setStatus('P',hikingEnterRequest.getStartTime());
            partyUserRepository.save(partyUser);

            //Mountain 서버한테 부탁해서 등산로 좌표 가져오기
            resp = getHikingTrack(party);
        }
        return resp;
    }

    private void alertHikingStart(HikingEnterRequest hikingEnterRequest, Party party) {
        List<Integer> partyMembers = partyUserRepository.findUserIdsByPartyIdAndStatus(hikingEnterRequest.getPartyId(), 'B');
        CommonHikingStartFeignRequest commonRequestDto=CommonHikingStartFeignRequest.builder()
                .type("hiking_start")
                .targetUserIds(partyMembers)
                .title("소모임 시작 알림")
                .message("'"+party.getPartyName()+"'"+" 소모임이 시작되었습니다. 입장해주세요!")
                .partyId(party.getPartyId())
                .dataSource("alarm")
                .alamType("PUSH")
                .build();
        log.info("[Party Server][Common Request] Alarm 한테 Party 시작 요청");
        ResponseEntity<?> responseEntity = hikingCommonClient.alertHiking(commonRequestDto);
        log.info("[Party Server][Common response ={}",responseEntity);
    }

    private HikingStartResponse getHikingTrack(Party party) {
        //소모임의 등산로id 리스트 꺼내기
        List<Integer> courseList=new ArrayList<>();
        String[] split = party.getSelectedCourse().split("\\.");
        for (String s : split) {
            courseList.add(Integer.parseInt(s));
        }
        MountainCourseFeignRequest mounainDto= MountainCourseFeignRequest.builder()
                .courseIdList(courseList)
                .build();
        log.info("[Party Server] Mountain 한테 등산로 좌표 조회 요청");
        ResponseEntity<?> mountainResp = hikingMountainClient.getCourse(mounainDto);
        if (mountainResp.getStatusCode().is2xxSuccessful()) {
            BasicResponse basicResponse = om.convertValue(mountainResp.getBody(), BasicResponse.class);
            if (basicResponse != null && basicResponse.getData() != null) {
                PartyTrackDataFeginRequest feignResp = om.convertValue(basicResponse.getData(), PartyTrackDataFeginRequest.class);
                List<LocationData> locationDataList = feignResp.getLocationDataList();
                log.info("[Party Server] Mountain 한테 등산로 좌표 조회 응답 받음 locationDataList={}", locationDataList);
                return new HikingStartResponse(feignResp.getDistance(),locationDataList);
            }
        } else {
            log.error("[Party Server] Mountain 한테 등산로 좌표 조회 요청 실패 mountainResp={}",mountainResp);
        }
        return null;
    }

    @Transactional
    public void hikingEnd(int userId,HikingExitRequest hikingExitRequest) {
        Party party = partyRepository.findByPartyId(hikingExitRequest.getPartyId())
                .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다."));
        if (party.getStatus() == 'B' || party.getStatus() == 'I')
            throw new PartyExpiredException("해당 소모임은 시작된 적이 없습니다.");
        if (party.getStatus() == 'E' && userId == party.getUserId()) {
            log.error("[Party Server][hikingEnd()]이미 끝난 소모임인데 소모임장이 퇴장 요청을 함 userId={}",userId);
            throw new DataMismatchException("이미 끝난 소모임인데 소모임장이 퇴장 요청을 함");
        }
        //소모임장이 hiking을 비활성화
        if (party.getStatus() == 'P' && userId == party.getUserId()) {
            //소모임 비활성화
            party.setPartyStatus('E');
            partyRepository.save(party);
            //소모임 회원들한테 알림보내기 요청
            alertHikingEnd(hikingExitRequest,party);
            //소모임장 퇴장 처리
            exitHiking(hikingExitRequest,userId,party.isLinked());
        }
        //P인데 파티원이 그냥 먼저 퇴장하거나, E라서 알림받고 파티원이 나가거나
        else {
            //파티원 퇴장 처리
            exitHiking(hikingExitRequest,userId,party.isLinked());
        }
    }

    private void alertHikingEnd(HikingExitRequest hikingExitRequest, Party party) {
        List<Integer> partyMembers = partyUserRepository.findUserIdsByPartyIdAndStatus(hikingExitRequest.getPartyId(), 'P');
        CommonHikingStartFeignRequest commonRequestDto=CommonHikingStartFeignRequest.builder()
                .type("hiking_end")
                .targetUserIds(partyMembers)
                .title("소모임 종료 알림")
                .message("'"+party.getPartyName()+"'"+" 소모임이 종료되었습니다. 즐거운 등반되셨나요?")
                .partyId(party.getPartyId())
                .dataSource(null)
                .alamType("POPUP")
                .build();
        log.info("[Party Server][Common Request] Alarm 한테 Party 종료 요청");
        ResponseEntity<?> responseEntity = hikingCommonClient.alertHiking(commonRequestDto);
        log.info("[Party Server][Common response ={}",responseEntity);
    }

    private void exitHiking(HikingExitRequest hikingExitRequest,int userId,boolean isLinked) {
        PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingExitRequest.getPartyId(), userId)
                .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));
        if (partyUser.getStatus() == 'E') throw new DataMismatchException("해당 유저는 이미 소모임을 종료했습니다.");
        //moveTime 갱신
        partyUser.setStatus('E', hikingExitRequest.getEndTime());
        partyUser.addHikingRecord(hikingExitRequest.getDistance(), hikingExitRequest.getBestHeight());
        partyUserRepository.save(partyUser);
        //Auth한테 포인트, 등산기록 정규화 요청
        boolean isFirstMountain = partyUserRepository.existsByUserIdAndMountainIdAndStatus(userId, partyUser.getMountainId(), 'E');
        HikingRecordRequest authDto= HikingRecordRequest.builder()
                .userId(userId)
                .distance(partyUser.getDistance())
                .moveTime(partyUser.getMoveTime())
                .isFirstMountain(isFirstMountain)
                .build();
        log.info("[Auth Server] Auth 한테 등산 기록 업데이트 요청");
        ResponseEntity<?> authResp = hikingAuthClient.patchRecord(authDto);
        if (authResp.getStatusCode().is2xxSuccessful()) {
            log.info("[Auth Server] Auth 한테 등산 기록 업데이트 요청 성공 authResp={}",authResp);
        }
        else{
            log.error("[Auth Server] Auth 한테 등산 기록 업데이트 요청 실패 authResp={}",authResp);
        }

        //랭킹 갱신
        if(isLinked){
            updateRank(hikingExitRequest, userId, partyUser);
        }
    }

    private void updateRank(HikingExitRequest hikingExitRequest, int userId, PartyUser partyUser) {
        //1. 최다등반
        String key1 = "guild/" + hikingExitRequest.getPartyId() + "/mostHiking";
        String value=Integer.toString(userId);
        redisTemplate.opsForZSet().incrementScore(key1, value, 1);
        //2. 최고고도
        String key2 = "guild/" + hikingExitRequest.getPartyId() + "/bestHeight";
        Integer newHeight= partyUser.getBestHeight();
        Double currentHeight = redisTemplate.opsForZSet().score(key2, value);
        if (currentHeight == null || currentHeight < newHeight) {
            redisTemplate.opsForZSet().add(key2, value, newHeight);
        }
        //3. 최장거리
        String key3 = "guild/" + hikingExitRequest.getPartyId() + "/bestDistance";
        Double currentDistance=redisTemplate.opsForZSet().score(key3, value);
        Double newDistance= partyUser.getDistance()+((currentDistance==null)?0:currentDistance);
        redisTemplate.opsForZSet().add(key3, value, newDistance);
    }

    @Transactional
    public void saveTrack(HikingTrackSaveFeignRequestDto hikingTrackSaveFeignRequestDto) {
        for (TrackData trackData : hikingTrackSaveFeignRequestDto.getTrackDataList()) {
            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingTrackSaveFeignRequestDto.getPartyId(), trackData.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));

//            테스트를 위해서 파티에 userId가 없으면 그냥 스킵(오류 말고)
//            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingTrackSaveReignRequestDto.getPartyId(), trackData.getUserId()).get();
//            if(partyUser==null) continue;

            //초기화할 때
            if (partyUser.getPoints() == null) {
                LineString lineString = GeometryUtils.createLineStringFromLocations(trackData.getLocationDataList());
                partyUser.addTrackPoints(lineString);
            } else {
                // 기존 좌표에 새로운 좌표 합치기
                List<Coordinate> existingCoordinates = getCoordinatesFromGeometry(partyUser.getPoints());
                List<Coordinate> allCoordinates = new ArrayList<>(existingCoordinates);
                trackData.getLocationDataList().forEach(loc ->
                        allCoordinates.add(new Coordinate(loc.getLng(), loc.getLat()))
                );
                LineString lineString = geometryFactory.createLineString(allCoordinates.toArray(new Coordinate[0]));
                partyUser.addTrackPoints(lineString);
            }
            partyUserRepository.save(partyUser);
//            //제대로 저장됐는지 확인하기(테스트용)
//            List<LocationData> locationData = GeometryUtils.convertGeometryToListLocationData(partyUser.getPoints());
//            for (LocationData locationDatum : locationData) {
//                log.info("locationDatum={},{}",locationDatum.getLat(),locationDatum.getLng());
//            }
        }
    }

    private List<Coordinate> getCoordinatesFromGeometry(Geometry geometry) {
        return List.of(geometry.getCoordinates());
    }


    public void hikingSafety(int userId, HikingSafetyRequest hikingSafetyRequest) {
        Party party = partyRepository.findByPartyId(hikingSafetyRequest.getPartyId())
                .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다."));

        List<Integer> partyMembers = partyUserRepository.findUserIdsByPartyId(hikingSafetyRequest.getPartyId());
        CommonHikingStartFeignRequest commonRequestDto=null;

        if ("off_course".equals(hikingSafetyRequest.getType())) {
            commonRequestDto=CommonHikingStartFeignRequest.builder()
                    .type(hikingSafetyRequest.getType())
                    .targetUserIds(partyMembers)
                    .title("소모임 위험 알림!!")
                    .message("'"+party.getPartyName()+"'"+" 소모임의 '"+userId+"님이 경로 이탈 신호를 보냈습니다.")
                    .partyId(party.getPartyId())
                    .dataSource("null")
                    .alamType("PUSH")
                    .lat(hikingSafetyRequest.getLat())
                    .lng(hikingSafetyRequest.getLng())
                    .build();
        }
        else if ("health_risk".equals(hikingSafetyRequest.getType())){
            commonRequestDto=CommonHikingStartFeignRequest.builder()
                    .type(hikingSafetyRequest.getType())
                    .targetUserIds(partyMembers)
                    .title("소모임 위험 알림!!")
                    .message("'"+party.getPartyName()+"'"+" 소모임의 '"+userId+"님이 건강위험 신호를 보냈습니다.")
                    .partyId(party.getPartyId())
                    .dataSource("safety_alert")
                    .alamType("POPUP")
                    .lat(hikingSafetyRequest.getLat())
                    .lng(hikingSafetyRequest.getLng())
                    .build();
        }
        else if ("fall_detection".equals(hikingSafetyRequest.getType())) {
            commonRequestDto=CommonHikingStartFeignRequest.builder()
                    .type(hikingSafetyRequest.getType())
                    .targetUserIds(partyMembers)
                    .title("소모임 위험 알림!!")
                    .message("'"+party.getPartyName()+"'"+" 소모임의 '"+userId+"님에게서 낙상이 감지 되었습니다.")
                    .partyId(party.getPartyId())
                    .dataSource("safety_alert")
                    .alamType("POPUP")
                    .lat(hikingSafetyRequest.getLat())
                    .lng(hikingSafetyRequest.getLng())
                    .build();
        }
        else {
            throw new IllegalArgumentException("해당 타입은 존재하지 않습니다.");
        }


        log.info("[Party Server][Common Request] Alarm 한테 위험감지 알림 요청");
        ResponseEntity<?> responseEntity = hikingCommonClient.alertHiking(commonRequestDto);
        log.info("[Party Server][Common response ={}",responseEntity);
    }
}
