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
    public void saveFcmToken(int userId, SaveTokenRequestDto saveTokenRequestDto){
        Optional<AlarmTokenEntity> alarmToken = alarmTokenRepository.findById(userId);
        if(alarmToken.isEmpty()){
            AlarmTokenEntity newalarmToken= AlarmTokenEntity.builder()
                    .Id(userId)
                    .fcmToken(saveTokenRequestDto.getFcmToken())
                    .build();
            alarmTokenRepository.save(newalarmToken);
            return;
        }
        alarmToken.get().setFcmToken(saveTokenRequestDto.getFcmToken());
        alarmTokenRepository.save(alarmToken.get());
    }
}
