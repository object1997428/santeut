package com.santeut.common.service;

import com.santeut.common.common.exception.AccessDeniedException;
import com.santeut.common.common.util.FcmUtils;
import com.santeut.common.common.util.GeoUtils;
import com.santeut.common.dto.FCMCategory;
import com.santeut.common.dto.FCMRequestDto;
import com.santeut.common.dto.request.AlarmRequestDto;
import com.santeut.common.dto.request.CommonHikingStartFeignRequest;
import com.santeut.common.entity.AlarmEntity;
import com.santeut.common.entity.AlarmTokenEntity;
import com.santeut.common.entity.SafetyAlertEntity;
import com.santeut.common.repository.AlarmRepository;
import com.santeut.common.repository.AlarmTokenRepository;
import com.santeut.common.repository.SafetyAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmTokenRepository alarmTokenRepository;
    private final SafetyAlertRepository safetyAlertRepository;
    private final FcmUtils fcmUtils;

    public void createAlarm(Integer referenceId, Character referenceType, AlarmRequestDto alarmRequestDto) {
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .userId(alarmRequestDto.getUserId())
                .referenceType(referenceType)
                .referenceId(referenceId)
                .alarmTitle(alarmRequestDto.getAlarmTitle())
                .alarmContent(alarmRequestDto.getAlarmContent()).build();
        alarmRepository.save(alarmEntity);
    }

    public void deleteAlarm(int alarmId) {
        try {
            alarmRepository.deleteById(alarmId);
        }catch (Exception e) {
            log.error("delete alarm error", e);
            throw new AccessDeniedException("삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void sendAlarm(CommonHikingStartFeignRequest hikingStartFeignRequest) {
        List<AlarmTokenEntity> alarmTokenList = alarmTokenRepository.findByIdIn(hikingStartFeignRequest.getTargetUserIds());

        for (AlarmTokenEntity alarmToken : alarmTokenList) {
            //알람 보내기
            fcmUtils.sendNotificationByToken(alarmToken, FCMRequestDto.of(hikingStartFeignRequest.getTitle(),
                    String.format(hikingStartFeignRequest.getMessage()),
                    FCMCategory.HIKING_START));

            if(hikingStartFeignRequest.getDataSource()==null) continue;
            if(hikingStartFeignRequest.getDataSource().equals("safety_alert")){
                SafetyAlertEntity safetyAlert= SafetyAlertEntity.builder()
                        .userId(alarmToken.getId())
                        .title(hikingStartFeignRequest.getTitle())
                        .message(hikingStartFeignRequest.getMessage())
                        .point(GeoUtils.createPoint(hikingStartFeignRequest.getLat(), hikingStartFeignRequest.getLng()))
                        .build();
                safetyAlertRepository.save(safetyAlert);
            }
            else if(hikingStartFeignRequest.getDataSource().equals("alarm")){
                AlarmEntity alarm=AlarmEntity.builder()
                        .userId(alarmToken.getId())
                        .referenceType('P')
                        .referenceId(hikingStartFeignRequest.getPartyId())
                        .alarmTitle(hikingStartFeignRequest.getTitle())
                        .alarmContent(hikingStartFeignRequest.getMessage())
                        .build();
                alarmRepository.save(alarm);
            }
        }
    }
}