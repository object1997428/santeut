package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alarm_token")
public class AlarmTokenEntity extends BaseEntity{
    @Id
    @NotNull
    @Column(name = "user_id")
    private Integer Id;

    @NotNull
    @Column(name = "fcm_token")
    private String fcmToken;

    //토큰 갱신
    public void updateToken(String fcmToken){
        this.fcmToken=fcmToken;
    }
}
