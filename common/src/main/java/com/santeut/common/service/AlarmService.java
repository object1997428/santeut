package com.santeut.common.service;

import com.santeut.common.common.exception.AccessDeniedException;
import com.santeut.common.dto.request.AlarmRequestDto;
import com.santeut.common.entity.AlarmEntity;
import com.santeut.common.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;

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
}