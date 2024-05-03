package com.santeut.party.service;

import com.santeut.party.common.exception.DataMismatchException;
import com.santeut.party.common.exception.DataNotFoundException;
import com.santeut.party.common.exception.PartyExpiredException;
import com.santeut.party.common.exception.PartyNotStartedException;
import com.santeut.party.common.util.GeometryUtils;
import com.santeut.party.dto.request.HikingTrackSaveReignRequestDto;
import com.santeut.party.dto.request.LocationData;
import com.santeut.party.dto.request.TrackData;
import com.santeut.party.entity.HikingEnterRequest;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.PartyUser;
import com.santeut.party.repository.PartyRepository;
import com.santeut.party.repository.PartyUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HikingService {
    private final PartyRepository partyRepository;
    private final PartyUserRepository partyUserRepository;
    private GeometryFactory geometryFactory=new GeometryFactory();
    public void hikingStart(HikingEnterRequest hikingEnterRequest){
        Party party = partyRepository.findByPartyId(hikingEnterRequest.getPartyId())
                .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다."));

        if(party.getStatus()=='E'||party.getStatus()=='I') throw new PartyExpiredException("해당 소모임은 이미 종료되었습니다.");
        if(party.getSchedule().isAfter(LocalDateTime.now())) throw new PartyNotStartedException("해당 소모임은 아직 시작할 수 없습니다.");
        if(party.getStatus()=='B'){
            if(hikingEnterRequest.getUserId()!=party.getUserId()) throw new PartyNotStartedException("아직 소모임장이 소모임을 활성화 하지 않았습니다.");
            //소모임 활성화
            party.setPartyStatus('P');
            partyRepository.save(party);
            //소모임 회원들한테 알림보내기 요청
            log.info("[Party Server] Alarm 한테 요청");

            //소모임장 입장 처리
            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingEnterRequest.getPartyId(), hikingEnterRequest.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));
            if(partyUser.getStatus()=='P'){
                log.error("Party가 시작 전(B)인데 PartyUser가 진행 중(P)이다");
                throw new DataMismatchException("소모임이 시작하지 않았는데 회원이 이미 소모임을 진행 중");
            }
            partyUser.setStatus('P');
            partyUserRepository.save(partyUser);
            return;
        }


        if(party.getStatus()=='P'){
            //입장 처리
            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingEnterRequest.getPartyId(), hikingEnterRequest.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));
            if(partyUser.getStatus()=='P') throw new DataMismatchException("해당 유저는 이미 소모임을 진행 중입니다.");
            partyUser.setStatus('P');
            partyUserRepository.save(partyUser);
        }
    }

    @Transactional
    public void saveTrack(HikingTrackSaveReignRequestDto hikingTrackSaveReignRequestDto) {
        for (TrackData trackData : hikingTrackSaveReignRequestDto.getTrackDataList()) {
//            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingTrackSaveReignRequestDto.getPartyId(), trackData.getUserId())
//                    .orElseThrow(() -> new DataNotFoundException("해당 소모임이나 유저가 존재하지 않습니다."));

            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(hikingTrackSaveReignRequestDto.getPartyId(), trackData.getUserId()).get();
            if(partyUser==null) continue;;

            //초기화할 때
            if(partyUser.getPoints()==null){
                LineString lineString = GeometryUtils.createLineStringFromLocations(trackData.getLocationDataList());
                partyUser.addTrackPoints(lineString);
            }
            else{
                // 기존 좌표에 새로운 좌표 합치기
                List<Coordinate> existingCoordinates  = getCoordinatesFromGeometry(partyUser.getPoints());
                List<Coordinate> allCoordinates = new ArrayList<>(existingCoordinates);
                trackData.getLocationDataList().forEach(loc ->
                        allCoordinates.add(new Coordinate(loc.getLng(), loc.getLat()))
                );
                LineString lineString = geometryFactory.createLineString(allCoordinates.toArray(new Coordinate[0]));
                partyUser.addTrackPoints(lineString);
            }
            partyUserRepository.save(partyUser);
//            //제대로 저장됐는지 확인하기
//            List<LocationData> locationData = GeometryUtils.convertGeometryToListLocationData(partyUser.getPoints());
//            for (LocationData locationDatum : locationData) {
//                log.info("locationDatum={},{}",locationDatum.getLat(),locationDatum.getLng());
//            }
        }
    }
    private List<Coordinate> getCoordinatesFromGeometry(Geometry geometry) {
        return List.of(geometry.getCoordinates());
    }
}
