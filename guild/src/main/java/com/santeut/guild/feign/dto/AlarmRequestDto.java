package com.santeut.guild.feign.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRequestDto {

    private int userId;
    private String referenceType;
    private int referenceId;
    private String alarmTitle;
    private String alarmContent;
}
