package com.santeut.common.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.santeut.common.common.exception.FirebaseSettingFailException;
import com.santeut.common.dto.FCMRequestDto;
import com.santeut.common.entity.AlarmTokenEntity;
import com.santeut.common.repository.AlarmTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmUtils {

    private final FirebaseMessaging firebaseMessaging;
    private final AlarmTokenRepository alarmTokenRepository;
    private final ObjectMapper om;

    @Transactional
    public boolean sendNotificationByToken(AlarmTokenEntity receiver, FCMRequestDto reqDto) throws JsonProcessingException {


        Message message = null;
        if(reqDto.getType().equals("PUSH")){
            Notification notification = Notification.builder()
                    .setTitle(reqDto.getTitle())
                    .setBody(reqDto.getContent())
                    .build();
            message = Message.builder()
                    .setToken(receiver.getFcmToken())
                    .setNotification(notification)
                    .putData("category", reqDto.getCategory())
                    .build();
        }
        else if(reqDto.getType().equals("POPUP")){
            AlarmData alarmData = new AlarmData(reqDto.getTitle(), reqDto.getContent());
            String alarmDataJson = om.writeValueAsString(alarmData);
            message = Message.builder()
                    .setNotification(null)
                    .setToken(receiver.getFcmToken())
                    .putData("data",alarmDataJson)
                    .build();
            log.info("alarmData.toString()={}, userId={}, token={}",alarmData.toString(),receiver.getId(),receiver.getFcmToken());
        }
        else{
            log.info("해당 AlarmType이 존재하지 않습니다.");
            return false;
        }

        try {
            firebaseMessaging.send(message);
            return true;
        } catch (FirebaseMessagingException e) {
            if (e.getErrorCode() == ErrorCode.NOT_FOUND || e.getErrorCode() == ErrorCode.INVALID_ARGUMENT) {
                log.error("UNREGISTERED or NOT_FOUND error, Error Code: {}, receiver: {}", e.getErrorCode(),receiver.getId());
                alarmTokenRepository.deleteById(receiver.getId());
                return false;
            } else {
                e.printStackTrace();
                throw new FirebaseSettingFailException("Firebase 설정이 잘못되었습니다.");
            }
        }
    }

    @Data @AllArgsConstructor
    public class AlarmData{
        public String title;
        public String body;
    }
}