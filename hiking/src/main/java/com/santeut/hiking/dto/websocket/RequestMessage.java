package com.santeut.hiking.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestMessage {
    private String type; //locationShare, healthLisk, offCourse
    private Double lat;
    private Double lng;

}
