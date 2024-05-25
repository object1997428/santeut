package com.santeut.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
public class AlarmListResponseDto {
    private List<Alarm> alarmList;

    @Getter @Setter
    @AllArgsConstructor
    @Builder
    public static class Alarm {
        private String alarmTitle;
        private String alarmContent;
        private String referenceType;
        private int referenceId;
        private LocalDateTime createdAt;
    }
}
