package com.santeut.common.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FCMCategory {
    // 등산 알림
    HIKING_START("100", "등산이 시작됨"),
    HIKING_END("101", "등산이 종료됨");

    public final String code;
    public final String description;
}
