package com.santeut.hiking.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLocationResponseDto {
    private Long id;
    private String type;
    private Integer userId;
    private String userNickname;
    private Double lat;
    private Double lng;
}
