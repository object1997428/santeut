package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alarm_token")
@EntityListeners(AuditingEntityListener.class)
public class AlarmTokenEntity{
    @Id
    @NotNull
    @Column(name = "user_id")
    private Integer Id;

    @NotNull
    @Column(name = "fcm_token")
    private String fcmToken;

    @LastModifiedDate
    @NotNull
    @Column(name = "last_active_at")
    private LocalDateTime activeAt;


    @CreatedDate
    @NotNull
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    //토큰 갱신
    public void updateToken(String fcmToken){
        this.fcmToken=fcmToken;
        this.activeAt=LocalDateTime.now();
    }

    public void active(){
        this.activeAt=LocalDateTime.now();
    }


}
