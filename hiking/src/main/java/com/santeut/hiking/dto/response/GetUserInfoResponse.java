package com.santeut.hiking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoResponse {

    private int userId;
    private String userLoginId;
    private String userNickname;

}

