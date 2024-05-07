package com.santeut.common.service;

import com.santeut.common.dto.request.SaveTokenRequestDto;
import com.santeut.common.entity.AlarmTokenEntity;
import com.santeut.common.repository.AlarmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmTokenService {
    private final AlarmTokenRepository alarmTokenRepository;

    @Transactional
    public void saveFcmToken(int userId, SaveTokenRequestDto requestDto) {
        Optional<AlarmTokenEntity> alarmToken = alarmTokenRepository.findByFcmToken(requestDto.getFcmToken());
        //새로운 기기 or 처음 로그인
        if (alarmToken.isEmpty()) {
            //사용자의 토큰 (재)생성
            updateToken(userId, requestDto);
        }
        //기존 기기에 다른 사용자가 사용
        else if (alarmToken.get().getId() != userId) {
            //기존 기기 사용하던 사용자의 토큰도 삭제
            alarmTokenRepository.deleteByFcmToken(alarmToken.get().getFcmToken());
            //사용자의 토큰 (재)생성
            updateToken(userId, requestDto);
        }
        //기존 기기에 로그인 or 로그인 유지&앱 실행
        else {
            alarmToken.get().active();
        }

    }

    private void updateToken(int userId, SaveTokenRequestDto requestDto) {
        Optional<AlarmTokenEntity> userToken = alarmTokenRepository.findById(userId);
        if(userToken.isPresent()){
            alarmTokenRepository.deleteById(userId);
        }
        addNewToken(userId, requestDto);
    }

    private void addNewToken(int userId, SaveTokenRequestDto requestDto) {
        AlarmTokenEntity newalarmToken = AlarmTokenEntity.builder()
                .Id(userId)
                .fcmToken(requestDto.getFcmToken())
                .build();
        alarmTokenRepository.save(newalarmToken);
    }

}
