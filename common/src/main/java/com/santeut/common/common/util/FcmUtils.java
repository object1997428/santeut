package com.santeut.common.common.util;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.santeut.common.common.exception.FirebaseSettingFailException;
import com.santeut.common.dto.FCMRequestDto;
import com.santeut.common.entity.AlarmTokenEntity;
import com.santeut.common.repository.AlarmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmUtils {

    private final FirebaseMessaging firebaseMessaging;
    private AlarmTokenRepository alarmTokenRepository;

    public boolean sendNotificationByToken(AlarmTokenEntity receiver, FCMRequestDto reqDto) {
        Notification notification = Notification.builder()
                .setTitle(reqDto.getTitle())
                .setBody(reqDto.getContent())
                .build();

        Message message = Message.builder()
                .setToken(receiver.getFcmToken())
                .setNotification(notification)
                .putData("category", reqDto.getCategory())
                .build();

        try {
            firebaseMessaging.send(message);
//            notiRepository.save(NotificationEntity.from(reqDto, receiver));
            return true;
        } catch (FirebaseMessagingException e) {
            if (e.getErrorCode() == ErrorCode.NOT_FOUND || e.getErrorCode() == ErrorCode.INVALID_ARGUMENT) {
                log.error("UNREGISTERED or NOT_FOUND error, Error Code: {}, receiver: {}", e.getErrorCode(),receiver.getId());
                receiver.inactive();
                return false;
            } else {
                e.printStackTrace();
                throw new FirebaseSettingFailException("Firebase 설정이 잘못되었습니다.");
            }
        }
    }
}