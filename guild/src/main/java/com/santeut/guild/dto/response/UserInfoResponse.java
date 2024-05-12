package com.santeut.guild.dto.response;

import com.santeut.guild.common.response.BasicResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class UserInfoResponse {
    int userId;
    String userLoginId;
    String userNickname;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    String userBirth;
    char userGender;
    LocalDateTime deletedAt;
    boolean isDeleted;
    String userProfile;

}
