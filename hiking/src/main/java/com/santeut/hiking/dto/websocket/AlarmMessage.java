package com.santeut.hiking.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmMessage {//POPUP할 때만 message로 alarm 보냄
    private String type;
    private String title;
    private String content;
}
