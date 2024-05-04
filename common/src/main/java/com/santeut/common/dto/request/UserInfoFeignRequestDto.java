package com.santeut.common.dto.request;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoFeignRequestDto {
    private int userId;
    private String userNickname;
}
