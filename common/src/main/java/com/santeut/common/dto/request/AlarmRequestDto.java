package com.santeut.common.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
public class AlarmRequestDto {

    private int userId;

    private int referenceType;

    private int referenceId;

    private String alarmTitle;

    private String alarmContent;
}

