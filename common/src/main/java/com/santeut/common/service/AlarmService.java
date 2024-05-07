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

import java.time.LocalDate;
import java.time.Period;
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
    public void sendAlarm(CommonHikingStartFeignRequest alertRequest) {
        List<AlarmTokenEntity> alarmTokenList = alarmTokenRepository.findByIdIn(alertRequest.getTargetUserIds());

        for (AlarmTokenEntity alarmToken : alarmTokenList) {
            //20일 지난 토큰은 비활성화하고 보내지 않음
            if(Period.between(alarmToken.getActiveAt().toLocalDate(), LocalDate.now()).getDays()>=20){
                alarmTokenRepository.deleteById(alarmToken.getId());
                continue;
            }
            //알람 보내기
            fcmUtils.sendNotificationByToken(alarmToken, FCMRequestDto.of(alertRequest.getTitle(),
                    String.format(alertRequest.getMessage()),
                    FCMCategory.HIKING_START));

            if(alertRequest.getDataSource()==null) continue;
            if(alertRequest.getDataSource().equals("safety_alert")){
                SafetyAlertEntity safetyAlert= SafetyAlertEntity.builder()
                        .userId(alarmToken.getId())
                        .title(alertRequest.getTitle())
                        .message(alertRequest.getMessage())
                        .point(GeoUtils.createPoint(alertRequest.getLat(), alertRequest.getLng()))
                        .build();
                safetyAlertRepository.save(safetyAlert);
            }
            else if(alertRequest.getDataSource().equals("alarm")){
                AlarmEntity alarm=AlarmEntity.builder()
                        .userId(alarmToken.getId())
                        .referenceType('P')
                        .referenceId(alertRequest.getPartyId())
                        .alarmTitle(alertRequest.getTitle())
                        .alarmContent(alertRequest.getMessage())
                        .build();
                alarmRepository.save(alarm);
            }
        }
    }
}