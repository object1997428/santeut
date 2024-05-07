package com.santeut.common.common.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.santeut.common.common.exception.FirebaseSettingFailException;
import com.santeut.common.dto.FCMRequestDto;
import com.santeut.common.entity.AlarmTokenEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmUtils {

    private final FirebaseMessaging firebaseMessaging;
//    private final NotiRepository notiRepository;

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
            log.error("e.getErrorCode()={}",e.getErrorCode());
            if ("UNREGISTERED".equals(e.getErrorCode())||"NOT_FOUND".equals(e.getErrorCode())) {
                log.error("UNREGISTERED!!, e.getErrorCode()={}",e.getErrorCode());
                return false;
            } else {
                e.printStackTrace();
                throw new FirebaseSettingFailException("Firebase 설정이 잘못되었습니다.");
            }
        }
    }
}